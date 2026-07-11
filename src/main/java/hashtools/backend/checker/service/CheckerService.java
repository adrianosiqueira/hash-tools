package hashtools.backend.checker.service;

import hashtools.backend.core.checksum.Checksum;
import hashtools.backend.core.source.ChecksumSource;
import hashtools.backend.core.source.InputSource;
import hashtools.backend.core.threadpool.ThreadPool;
import hashtools.backend.checker.domain.CheckerChecksum;
import hashtools.backend.checker.domain.ChecksumCheckingContainer;
import hashtools.backend.checker.domain.ChecksumCheckingParameter;
import hashtools.backend.checker.domain.ChecksumCheckingResult;

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



        try {
            // Processing data
            List<Checksum> officialChecksums = checksumSource.extractOfficialChecksums();
            List<Future<CheckerChecksum>> futureChecksums = new ArrayList<>();
            ChecksumCheckingResult result = new ChecksumCheckingResult();

            // Progress tracking
            AtomicInteger totalTasks = new AtomicInteger(officialChecksums.size());
            AtomicInteger completedTasks = new AtomicInteger(0);



            // Parallel checksum generation
            for (Checksum official : officialChecksums) {
                futureChecksums.add(ThreadPool.FIXED_DAEMON.submit(() -> {
                    Checksum generated = official
                        .getAlgorithm()
                        .generateChecksum(inputSource::updateMessageDigest);



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
