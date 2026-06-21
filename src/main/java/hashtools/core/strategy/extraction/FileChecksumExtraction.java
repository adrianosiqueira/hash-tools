package hashtools.core.strategy.extraction;

import hashtools.core.checksum.Checksum;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class FileChecksumExtraction implements ChecksumExtraction {

    private Path file;



    public FileChecksumExtraction(String filePath) {
        Objects.requireNonNull(filePath);
        this.file = Path.of(filePath);
    }



    @Override
    public List<Checksum> extract() throws RuntimeException {
        try (Stream<String> lines = Files.lines(file)) {
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
