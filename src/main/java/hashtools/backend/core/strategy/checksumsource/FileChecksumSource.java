package hashtools.backend.core.strategy.checksumsource;

import hashtools.backend.core.checksum.Checksum;
import hashtools.backend.core.file.EnhancedFile;
import hashtools.backend.core.file.FileExtension;
import hashtools.backend.core.interfaces.ChecksumSource;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class FileChecksumSource implements ChecksumSource {

    private EnhancedFile file;



    public FileChecksumSource(String filePath) {
        this.file = EnhancedFile
            .filePath(filePath)
            .orElse(null);
    }



    @Override
    public List<Checksum> extractOfficialChecksums() throws RuntimeException {
        try (Stream<String> lines = file.getLines()) {
            return lines
                .map(line -> line.split(" ")[0])
                .map(Checksum::new)
                .filter(Checksum::isValid)
                .toList();
        } catch (IOException e) {
            throw new RuntimeException("Failed to extract the official checksums. Check if the file is valid.", e);
        }
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
}
