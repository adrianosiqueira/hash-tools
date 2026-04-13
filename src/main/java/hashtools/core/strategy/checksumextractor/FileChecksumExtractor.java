package hashtools.core.strategy.checksumextractor;

import hashtools.core.model.Checksum;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class FileChecksumExtractor implements ChecksumExtractor {

    private final String filePath;



    public FileChecksumExtractor(String filePath) {
        this.filePath = Optional
            .ofNullable(filePath)
            .orElse("");
    }



    @Override
    public List<Checksum> extract() {
        Path file = Path.of(filePath);

        try (Stream<String> lines = Files.lines(file)) {

            return lines
                .map(this::getChecksumFromLine)
                .map(Checksum::new)
                .filter(Checksum::isValid)
                .toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    private String getChecksumFromLine(String line) {
        return line.split(" ")[0];
    }
}
