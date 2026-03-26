package hashtools.core.strategy.checksumextractor;

import hashtools.core.model.Checksum;

import java.util.List;

public interface ChecksumExtractor {

    static ChecksumExtractor nullImplementation() {
        return List::of;
    }



    List<Checksum> extract();
}
