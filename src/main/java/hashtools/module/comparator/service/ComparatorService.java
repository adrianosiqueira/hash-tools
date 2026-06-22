package hashtools.module.comparator.service;

import hashtools.core.checksum.Algorithm;
import hashtools.core.checksum.Checksum;
import hashtools.core.source.InputSource;
import hashtools.core.threadpool.ThreadPoolFactory;
import hashtools.module.comparator.domain.ChecksumComparisonCallback;
import hashtools.module.comparator.domain.ChecksumComparisonParameter;
import hashtools.module.comparator.domain.ChecksumComparisonResult;
import hashtools.module.comparator.domain.ComparatorChecksum;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ComparatorService {

    public void performChecksumComparison(ChecksumComparisonParameter parameter, ChecksumComparisonCallback callback) {
        InputSource inputSource1 = parameter.getInputSource1();
        InputSource inputSource2 = parameter.getInputSource2();



        // Problem detection
        String problem = inputSource1
            .detectProblem()
            .or(inputSource2::detectProblem)
            .orElse(null);

        if (problem != null) {
            callback.sendProblem(problem);
            return;
        }



        try (ExecutorService threadPool = ThreadPoolFactory.fixedDaemonPool()) {
            // Processing data
            Algorithm algorithm = parameter.getAlgorithm();
            ChecksumComparisonResult result = new ChecksumComparisonResult();



            // Parallel checksum generation
            Future<Checksum> futureChecksum1 = threadPool.submit(() -> algorithm.generateChecksum(inputSource1::updateMessageDigest));
            Future<Checksum> futureChecksum2 = threadPool.submit(() -> algorithm.generateChecksum(inputSource2::updateMessageDigest));



            // Result collecting
            ComparatorChecksum checksum = new ComparatorChecksum();
            checksum.setChecksum1(futureChecksum1.get());
            checksum.setChecksum2(futureChecksum2.get());

            result.setChecksum(checksum);
            callback.sendResult(result);
        } catch (ExecutionException | InterruptedException e) {
            callback.sendException(e);
        }
    }
}
