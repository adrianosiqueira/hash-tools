package hashtools.checker.domain;

import hashtools.checker.strategy.ChecksumExtraction;
import hashtools.core.domain.ProblemException;
import hashtools.core.strategy.ProblemDetection;

import java.io.IOException;
import java.util.Objects;

public class ChecksumSource {

    private ProblemDetection problemDetection;
    private ChecksumExtraction checksumExtraction;



    private ChecksumSource(ProblemDetection problemDetection, ChecksumExtraction checksumExtraction) {
        this.problemDetection = problemDetection;
        this.checksumExtraction = checksumExtraction;
    }



    public ChecksumSource fileChecksumSource(String filePath) {
        Objects.requireNonNull(filePath);

        // TODO Add the real implementation
        return new ChecksumSource(
            null,
            null
        );
    }

    public ChecksumSource stringChecksumSource(String string) {
        Objects.requireNonNull(string);

        // TODO Add the real implementation
        return new ChecksumSource(
            null,
            null
        );
    }



    public void detectProblem()
    throws ProblemException {
        problemDetection.detect();
    }

    public OfficialChecksums extractOfficialChecksums()
    throws IOException {
        return checksumExtraction.extract();
    }
}
