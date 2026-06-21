package hashtools.core.source;

import hashtools.core.strategy.extraction.ChecksumExtraction;
import hashtools.core.strategy.extraction.FileChecksumExtraction;
import hashtools.core.strategy.extraction.TextChecksumExtraction;
import hashtools.core.strategy.problem.FileChecksumProblemDetection;
import hashtools.core.strategy.problem.ProblemDetection;
import hashtools.core.strategy.problem.TextChecksumProblemDetection;

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
            new FileChecksumProblemDetection(filePath)
        );
    }

    public static ChecksumSource textChecksumSource(String text) {
        return new ChecksumSource(
            new TextChecksumExtraction(text),
            new TextChecksumProblemDetection(text)
        );
    }



    public ChecksumExtraction getChecksumExtraction() {
        return checksumExtraction;
    }

    public ProblemDetection getProblemDetection() {
        return problemDetection;
    }
}
