package hashtools.backend.core.strategy.checksumsource;

import hashtools.backend.core.checksum.Checksum;
import hashtools.backend.core.interfaces.ChecksumSource;

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
