package hashtools.backend.comparator.service;

import hashtools.backend.core.checksum.Algorithm;
import hashtools.backend.core.checksum.Checksum;
import hashtools.backend.core.source.InputSource;
import hashtools.backend.core.threadpool.ThreadPool;
import hashtools.backend.comparator.domain.ChecksumComparisonContainer;
import hashtools.backend.comparator.domain.ChecksumComparisonParameter;
import hashtools.backend.comparator.domain.ChecksumComparisonResult;
import hashtools.backend.comparator.domain.ComparatorChecksum;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class ComparatorService {

    public ChecksumComparisonContainer performChecksumComparison(ChecksumComparisonParameter parameter) {
        // Initial setup
        parameter.updateProgress(0.0);

        InputSource inputSource1 = parameter.getInputSource1();
        InputSource inputSource2 = parameter.getInputSource2();



        // Problem detection
        String problem = inputSource1
            .detectProblem()
            .or(inputSource2::detectProblem)
            .orElse(null);

        if (problem != null) {
            return ChecksumComparisonContainer.problem(problem);
        }



        try {
            // Processing data
            Algorithm algorithm = parameter.getAlgorithm();
            ChecksumComparisonResult result = new ChecksumComparisonResult();

            // Progress tracking
            AtomicInteger totalTasks = new AtomicInteger(2);
            AtomicInteger completedTasks = new AtomicInteger(0);



            // Parallel checksum generation
            Future<Checksum> futureChecksum1 = ThreadPool.FIXED_DAEMON.submit(() -> {
                Checksum checksum = algorithm.generateChecksum(inputSource1::updateMessageDigest);

                synchronized (completedTasks) {
                    double progress = completedTasks.incrementAndGet() / totalTasks.doubleValue();
                    parameter.updateProgress(progress);
                }

                return checksum;
            });

            Future<Checksum> futureChecksum2 = ThreadPool.FIXED_DAEMON.submit(() -> {
                Checksum checksum = algorithm.generateChecksum(inputSource2::updateMessageDigest);

                synchronized (completedTasks) {
                    double progress = completedTasks.incrementAndGet() / totalTasks.doubleValue();
                    parameter.updateProgress(progress);
                }

                return checksum;
            });



            // Result collecting
            ComparatorChecksum checksum = new ComparatorChecksum();
            checksum.setChecksum1(futureChecksum1.get());
            checksum.setChecksum2(futureChecksum2.get());

            result.setChecksum(checksum);

            parameter.updateProgress(1.0);
            return ChecksumComparisonContainer.result(result);
        } catch (ExecutionException | InterruptedException e) {
            return ChecksumComparisonContainer.exception(e);
        }
    }
}
