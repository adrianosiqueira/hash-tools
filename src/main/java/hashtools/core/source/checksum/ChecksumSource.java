package hashtools.core.source.checksum;

import hashtools.core.checksum.Checksum;
import hashtools.core.problem.Problem;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ChecksumSource {

    Optional<Problem> checkForProblem();

    List<Checksum> getValidChecksums() throws IOException;
}
