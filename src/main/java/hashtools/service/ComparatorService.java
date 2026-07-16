package hashtools.service;

import hashtools.domain.container.ChecksumComparisonContainer;
import hashtools.domain.context.ChecksumComparisonParameter;
import hashtools.domain.result.ChecksumComparisonResult;
import hashtools.domain.checksum.ComparatorChecksum;
import hashtools.domain.checksum.Algorithm;
import hashtools.domain.checksum.Checksum;
import hashtools.strategy.inputsource.InputSource;
import hashtools.backend.core.strategy.threadpool.ThreadPool;
import hashtools.backend.core.strategy.threadpool.AllCoreDaemonThreadPool;

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



        try (ThreadPool threadPool = new AllCoreDaemonThreadPool()) {
            // Processing data
            Algorithm algorithm = parameter.getAlgorithm();
            ChecksumComparisonResult result = new ChecksumComparisonResult();

            // Progress tracking
            AtomicInteger totalTasks = new AtomicInteger(2);
            AtomicInteger completedTasks = new AtomicInteger(0);



            // Parallel checksum generation
            Future<Checksum> futureChecksum1 = threadPool.run(() -> {
                Checksum checksum = algorithm.generateChecksum(inputSource1);

                synchronized (completedTasks) {
                    double progress = completedTasks.incrementAndGet() / totalTasks.doubleValue();
                    parameter.updateProgress(progress);
                }

                return checksum;
            });

            Future<Checksum> futureChecksum2 = threadPool.run(() -> {
                Checksum checksum = algorithm.generateChecksum(inputSource2);

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
