package hashtools.core.strategy.checksumextractor;

import hashtools.core.model.Checksum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class FileChecksumExtractor implements ChecksumExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileChecksumExtractor.class);



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
            List<Checksum> checksums = lines
                .map(this::getChecksumFromLine)
                .map(Checksum::new)
                .filter(Checksum::isValid)
                .toList();

            LOGGER.info("Extracted '{}' checksums from '{}'.", checksums.size(), filePath);
            return checksums;
        } catch (Exception e) {
            LOGGER.error("Failed to extract checksums from '{}'.", filePath, e);
            throw new RuntimeException(e);
        }
    }



    private String getChecksumFromLine(String line) {
        return line.split(" ")[0];
    }
}
