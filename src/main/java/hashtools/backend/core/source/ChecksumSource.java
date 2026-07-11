package hashtools.backend.core.source;

import hashtools.backend.core.checksum.Checksum;
import hashtools.backend.core.strategy.extraction.ChecksumExtraction;
import hashtools.backend.core.strategy.extraction.FileChecksumExtraction;
import hashtools.backend.core.strategy.extraction.TextChecksumExtraction;
import hashtools.backend.core.strategy.problem.ChecksumFileProblemDetection;
import hashtools.backend.core.strategy.problem.ProblemDetection;
import hashtools.backend.core.strategy.problem.ChecksumTextProblemDetection;

import java.util.List;
import java.util.Optional;

public class ChecksumSource {

    private ChecksumExtraction checksumExtraction;
    private ProblemDetection problemDetection;



    private ChecksumSource(ChecksumExtraction checksumExtraction, ProblemDetection problemDetection) {
        this.checksumExtraction = checksumExtraction;
        this.problemDetection = problemDetection;
    }



    public static ChecksumSource fileChecksumSource(String filePath) {
        return new ChecksumSource(
            new FileChecksumExtraction(filePath),
            new ChecksumFileProblemDetection(filePath)
        );
    }

    public static ChecksumSource textChecksumSource(String text) {
        return new ChecksumSource(
            new TextChecksumExtraction(text),
            new ChecksumTextProblemDetection(text)
        );
    }

    public static ChecksumSource nullChecksumSource() {
        return new ChecksumSource(
            List::of,
            Optional::empty
        );
    }



    public ChecksumExtraction getChecksumExtraction() {
        return checksumExtraction;
    }

    public ProblemDetection getProblemDetection() {
        return problemDetection;
    }



    public List<Checksum> extractOfficialChecksums() throws RuntimeException {
        return checksumExtraction.extract();
    }

    public Optional<String> detectProblem() {
        return problemDetection.detect();
    }
}
