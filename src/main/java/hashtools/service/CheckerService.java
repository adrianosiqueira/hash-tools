package hashtools.service;

import hashtools.domain.checksum.CheckerChecksum;
import hashtools.domain.container.ChecksumCheckingContainer;
import hashtools.domain.context.ChecksumCheckingParameter;
import hashtools.domain.result.ChecksumCheckingResult;
import hashtools.domain.checksum.Checksum;
import hashtools.strategy.checksumsource.ChecksumSource;
import hashtools.strategy.inputsource.InputSource;
import hashtools.backend.core.strategy.threadpool.ThreadPool;
import hashtools.backend.core.strategy.threadpool.AllCoreDaemonThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class CheckerService {

    public ChecksumCheckingContainer performChecksumChecking(ChecksumCheckingParameter parameter) {
        // Initial setup
        parameter.updateProgress(0.0);

        InputSource inputSource = parameter.getInputSource();
        ChecksumSource checksumSource = parameter.getChecksumSource();



        // Problem detection
        String problem = inputSource
            .detectProblem()
            .or(checksumSource::detectProblem)
            .orElse(null);

        if (problem != null) {
            return ChecksumCheckingContainer.problem(problem);
        }



        try (ThreadPool threadPool = new AllCoreDaemonThreadPool()) {
            // Processing data
            List<Checksum> officialChecksums = checksumSource.extractOfficialChecksums();
            List<Future<CheckerChecksum>> futureChecksums = new ArrayList<>();
            ChecksumCheckingResult result = new ChecksumCheckingResult();

            // Progress tracking
            AtomicInteger totalTasks = new AtomicInteger(officialChecksums.size());
            AtomicInteger completedTasks = new AtomicInteger(0);



            // Parallel checksum generation
            for (Checksum official : officialChecksums) {
                futureChecksums.add(threadPool.run(() -> {
                    Checksum generated = official
                        .getAlgorithm()
                        .generateChecksum(inputSource);



                    synchronized (completedTasks) {
                        double progress = completedTasks.incrementAndGet() / totalTasks.doubleValue();
                        parameter.updateProgress(progress);
                    }



                    CheckerChecksum checksum = new CheckerChecksum();
                    checksum.setOfficial(official);
                    checksum.setGenerated(generated);

                    return checksum;
                }));
            }



            // Result collecting
            for (Future<CheckerChecksum> checksum : futureChecksums) {
                result.addChecksum(checksum.get());
            }

            parameter.updateProgress(1.0);
            return ChecksumCheckingContainer.result(result);
        } catch (ExecutionException | InterruptedException e) {
            return ChecksumCheckingContainer.exception(e);
        }
    }
}
