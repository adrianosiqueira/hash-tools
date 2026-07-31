package hashtools.strategy.checksumextraction;

import hashtools.domain.checksum.Checksum;
import hashtools.domain.file.EnhancedFile;

import java.util.Collection;
import java.util.stream.Stream;

public class FileChecksumExtraction implements ChecksumExtraction {

    private EnhancedFile file;



    public FileChecksumExtraction(String filePath) {
        this.file = EnhancedFile.createFromFilePath(filePath);
    }



    @Override
    public Result extractOfficialChecksums() {
        try (Stream<String> lines = file.getLinesStream()) {
            Collection<Checksum> checksums = lines
                .map(line -> line.split(" ")[0])
                .map(Checksum::createFromHash)
                .filter(Checksum::isValid)
                .toList();

            return new Result.Success(checksums);
        } catch (Exception e) {
            return new Result.Failure(e);
        }
    }
}
