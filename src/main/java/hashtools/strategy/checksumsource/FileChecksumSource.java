package hashtools.strategy.checksumsource;

import hashtools.domain.checksum.Checksum;
import hashtools.domain.file.EnhancedFile;
import hashtools.domain.file.FileExtension;
import hashtools.domain.result.CanceledResult;
import hashtools.domain.result.ExceptionResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public class FileChecksumSource implements ChecksumSource {

    private EnhancedFile file;
    private boolean canceled;



    public FileChecksumSource(String filePath) {
        this.file = EnhancedFile.createFromFilePath(filePath);
        this.canceled = false;
    }



    @Override
    public Result extractOfficialChecksums() {
        Collection<Checksum> checksums = new ArrayList<>();

        try (Stream<String> lines = file.getLinesStream()) {
            lines.forEach(line -> {
                if (this.isCanceled()) {
                    return;
                }

                String hash = line.split(" ")[0];
                Checksum checksum = Checksum.createFromHash(hash);

                if (checksum.isValid()) {
                    checksums.add(checksum);
                }
            });
        } catch (Exception e) {
            return new ExceptionResult(e);
        }

        return this.isCanceled()
            ? new CanceledResult()
            : new SuccessResult(checksums);
    }

    @Override
    public void cancelChecksumsExtraction() {
        canceled = true;
    }

    @Override
    public Optional<String> detectProblem() {
        if (!file.hasFileExtension(FileExtension.HASH)) {
            return Optional.of("The file is not a checksum file. Use the dialog selector to select a valid file.");
        } else if (!file.exists()) {
            return Optional.of("The checksum file does not exist. Use the dialog selector to select a valid file.");
        } else if (!file.isRegularFile()) {
            return Optional.of("The checksum file is not a regular file. Use the dialog selector to select a valid file.");
        }

        return Optional.empty();
    }



    private boolean isCanceled() {
        return canceled;
    }
}
