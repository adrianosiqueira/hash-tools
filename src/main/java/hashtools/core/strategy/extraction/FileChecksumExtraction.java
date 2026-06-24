package hashtools.core.strategy.extraction;

import hashtools.core.checksum.Checksum;
import hashtools.core.file.EnhancedFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class FileChecksumExtraction implements ChecksumExtraction {

    private EnhancedFile file;



    public FileChecksumExtraction(String filePath) {
        this.file = EnhancedFile
            .filePath(filePath)
            .orElseThrow();
    }



    @Override
    public List<Checksum> extract() throws RuntimeException {
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
}
