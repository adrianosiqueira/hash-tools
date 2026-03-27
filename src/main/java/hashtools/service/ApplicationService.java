package hashtools.service;

import hashtools.module.checking.ChecksumChecking;
import hashtools.module.checking.ChecksumCheckingContext;
import hashtools.module.checking.ChecksumCheckingResult;
import hashtools.module.comparison.ChecksumComparison;
import hashtools.module.comparison.ChecksumComparisonContext;
import hashtools.module.comparison.ChecksumComparisonResult;
import hashtools.module.generation.ChecksumGeneration;
import hashtools.module.generation.ChecksumGenerationContext;
import hashtools.module.generation.ChecksumGenerationResult;

public class ApplicationService {

    private ChecksumChecking checksumChecking;
    private ChecksumComparison checksumComparison;
    private ChecksumGeneration checksumGeneration;



    public ChecksumCheckingResult performChecksumChecking(ChecksumCheckingContext context) {
        if (checksumChecking == null) {
            checksumChecking = new ChecksumChecking();
        }

        return checksumChecking.perform(context);
    }

    public ChecksumComparisonResult performChecksumComparison(ChecksumComparisonContext context) {
        if (checksumComparison == null) {
            checksumComparison = new ChecksumComparison();
        }

        return checksumComparison.perform(context);
    }

    public ChecksumGenerationResult performChecksumGeneration(ChecksumGenerationContext context) {
        if (checksumGeneration == null) {
            checksumGeneration = new ChecksumGeneration();
        }

        return checksumGeneration.perform(context);
    }
}
