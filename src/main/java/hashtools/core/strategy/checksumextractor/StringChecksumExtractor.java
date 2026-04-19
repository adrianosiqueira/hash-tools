package hashtools.core.strategy.checksumextractor;

import hashtools.core.model.Checksum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class StringChecksumExtractor implements ChecksumExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringChecksumExtractor.class);



    private final String string;



    public StringChecksumExtractor(String string) {
        this.string = Optional
            .ofNullable(string)
            .orElse("");
    }



    @Override
    public List<Checksum> extract() {
        List<Checksum> checksums = Stream
            .of(new Checksum(string))
            .filter(Checksum::isValid)
            .toList();

        LOGGER.info("Extracted '{}' checksums from '{}'.", checksums.size(), string);
        return checksums;
    }
}
