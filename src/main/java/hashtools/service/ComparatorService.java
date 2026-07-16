package hashtools.service;

import hashtools.domain.algorithm.MessageDigestProxy;
import hashtools.domain.algorithm.Algorithm;
import hashtools.domain.checksum.ComparatorChecksum;
import hashtools.domain.container.ChecksumComparisonContainer;
import hashtools.domain.context.ChecksumComparisonContext;
import hashtools.domain.result.ChecksumComparisonResult;

import java.io.IOException;
import java.util.List;

public class ComparatorService {

    public ChecksumComparisonContainer performChecksumComparison(ChecksumComparisonContext context) {
        // Problem detection
        String problem = context
            .detectProblem()
            .orElse(null);

        if (problem != null) {
            return ChecksumComparisonContainer.problem(problem);
        }



        // Processing data
        Algorithm algorithm = context.getAlgorithm();
        MessageDigestProxy messageDigestProxy1 = MessageDigestProxy.fromAlgorithm(algorithm);
        MessageDigestProxy messageDigestProxy2 = MessageDigestProxy.fromAlgorithm(algorithm);



        try {
            // Processing
            context.updateMessageDigests1(List.of(messageDigestProxy1.messageDigest()));
            context.updateMessageDigests2(List.of(messageDigestProxy2.messageDigest()));



            // Result collecting
            ComparatorChecksum checksum = new ComparatorChecksum();
            checksum.setChecksum1(messageDigestProxy1.decodeIntoChecksum());
            checksum.setChecksum2(messageDigestProxy2.decodeIntoChecksum());

            ChecksumComparisonResult result = new ChecksumComparisonResult();
            result.setChecksum(checksum);

            return ChecksumComparisonContainer.result(result);
        } catch (IOException e) {
            return ChecksumComparisonContainer.exception(e);
        }
    }
}
