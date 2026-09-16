package hashtools.strategy.checksumextraction;

import hashtools.domain.checksum.Checksum;
import hashtools.domain.commom.Result;
import hashtools.domain.file.EnhancedFile;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

public class FileChecksumExtraction implements ChecksumExtraction {

    private EnhancedFile file;



    public FileChecksumExtraction(String filePath) {
        this.file = EnhancedFile.createFromFilePath(filePath);
    }



    @Override
    public Result<Collection<Checksum>, String> extractChecksums() {
        try (Stream<String> lines = file.getLinesStream()) {
            var checksums = lines
                .map(line -> line.split(" ")[0])
                .map(Checksum::createFromHash)
                .filter(Checksum::isValid)
                .toList();

            return new Result.Ok<>(checksums);
        } catch (IOException e) {
            return new Result.Error<>(e.getMessage());
        } catch (Exception e) {
            return new Result.Error<>("Failed to extract checksums");
        }
    }
}
