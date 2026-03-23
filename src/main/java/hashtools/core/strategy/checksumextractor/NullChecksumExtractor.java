package hashtools.core.strategy.checksumextractor;

import hashtools.core.model.Checksum;

import java.util.List;

public class NullChecksumExtractor implements ChecksumExtractor {

    @Override
    public List<Checksum> extract() {
        throw new IllegalStateException("The ChecksumExtractor is not provided");
    }
}
