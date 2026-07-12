package hashtools.backend.core.interfaces;

import hashtools.backend.core.checksum.Checksum;

import java.util.List;
import java.util.Optional;

public interface ChecksumSource {

    List<Checksum> extractOfficialChecksums() throws RuntimeException;

    Optional<String> detectProblem();
}
