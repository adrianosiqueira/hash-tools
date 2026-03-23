package hashtools.core.strategy.checksumextractor;

import hashtools.core.model.Checksum;

import java.util.List;

public interface ChecksumExtractor {

    List<Checksum> extract();
}
