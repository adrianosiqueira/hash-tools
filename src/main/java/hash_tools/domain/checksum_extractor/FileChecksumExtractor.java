package hash_tools.domain.checksum_extractor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileChecksumExtractor extends ChecksumExtractor {

    private final Path file;



    public FileChecksumExtractor(Path file) {
        this.file = file;
    }



    @Override
    protected Stream<String> extractDataStream() {
        try (Stream<String> lines = Files.lines(file)) {
            return lines.map(this::retrieveChecksumFromLine);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
