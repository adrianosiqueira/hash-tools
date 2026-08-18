package hashtools.strategy.checksumextraction;

import hashtools.domain.checksum.Checksum;

import java.util.Collection;
import java.util.List;

public interface ChecksumExtraction {

    default Collection<Checksum> extract() throws RuntimeException {
        return List.of();
    }
}
