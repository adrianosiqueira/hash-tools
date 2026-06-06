package hashtools.core.source.checksum;

import hashtools.core.checksum.Checksum;
import hashtools.core.problem.Problem;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public interface ChecksumSource {

    boolean checkForProblem(Consumer<Problem> problemConsumer);

    List<Checksum> getValidChecksums() throws IOException;
}
