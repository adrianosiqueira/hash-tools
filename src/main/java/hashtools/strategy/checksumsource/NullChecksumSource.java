package hashtools.strategy.checksumsource;

import hashtools.domain.checksum.Checksum;

import java.util.List;
import java.util.Optional;

public class NullChecksumSource implements ChecksumSource {

    @Override
    public List<Checksum> extractOfficialChecksums() throws RuntimeException {
        return List.of();
    }

    @Override
    public Optional<String> detectProblem() {
        return Optional.empty();
    }
}
