package hashtools.strategy.checksumextraction;

import hashtools.domain.checksum.Checksum;
import hashtools.domain.commom.Result;

import java.util.Collection;
import java.util.List;

public interface ChecksumExtraction {

    @Deprecated(forRemoval = true)
    default Collection<Checksum> extract() throws RuntimeException {
        return List.of();
    }

    default Result<Collection<Checksum>, String> extractChecksums() {
        return new Result.Ok<>(List.of());
    }
}
