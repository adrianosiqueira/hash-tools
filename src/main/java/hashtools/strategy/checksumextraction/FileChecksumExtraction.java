package hashtools.strategy.checksumextraction;

import hashtools.domain.checksum.Checksum;
import hashtools.domain.file.EnhancedFile;

import java.util.Collection;
import java.util.stream.Stream;

public class FileChecksumExtraction implements ChecksumExtraction {

    private EnhancedFile file;



    public FileChecksumExtraction(String filePath) {
        this.file = EnhancedFile
            .filePath(filePath)
            .orElse(null);
    }



    @Override
    public Result extractOfficialChecksums() {
        try (Stream<String> lines = file.getLines()) {
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
