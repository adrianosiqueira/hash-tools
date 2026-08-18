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
    public Collection<Checksum> extract() throws RuntimeException {
        try (Stream<String> lines = file.getLinesStream()) {
            return lines
                .map(line -> line.split(" ")[0])
                .map(Checksum::createFromHash)
                .filter(Checksum::isValid)
                .toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
