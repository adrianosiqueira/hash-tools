package hashtools.strategy.checksumsource;

import hashtools.domain.checksum.Checksum;

import java.util.List;
import java.util.Optional;

public interface ChecksumSource {

    List<Checksum> extractOfficialChecksums() throws RuntimeException;

    Optional<String> detectProblem();
}
