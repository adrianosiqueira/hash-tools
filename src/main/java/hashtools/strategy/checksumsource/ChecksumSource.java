package hashtools.strategy.checksumsource;

import hashtools.domain.checksum.Checksum;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ChecksumSource {

    default List<Checksum> extractOfficialChecksums() throws IOException {
        return List.of();
    }

    default Optional<String> detectProblem() {
        return Optional.empty();
    }
}
