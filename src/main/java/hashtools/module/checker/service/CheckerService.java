package hashtools.module.checker.service;

import hashtools.core.checksum.Checksum;
import hashtools.core.communication.Callback;
import hashtools.core.source.ChecksumSource;
import hashtools.core.source.InputSource;
import hashtools.core.threadpool.ThreadPool;
import hashtools.module.checker.domain.CheckerChecksum;
import hashtools.module.checker.domain.ChecksumCheckingParameter;
import hashtools.module.checker.domain.ChecksumCheckingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class CheckerService {

    public void performChecksumChecking(ChecksumCheckingParameter parameter, Callback<ChecksumCheckingResult> callback) {
        InputSource inputSource = parameter.getInputSource();
        ChecksumSource checksumSource = parameter.getChecksumSource();



        // Problem detection
        String problem = inputSource
            .detectProblem()
            .or(checksumSource::detectProblem)
            .orElse(null);

        if (problem != null) {
            callback.sendProblem(problem);
            return;
        }



        try {
            // Processing data
            List<Checksum> officialChecksums = checksumSource.extractOfficialChecksums();
            List<Future<CheckerChecksum>> futureChecksums = new ArrayList<>();
            ChecksumCheckingResult result = new ChecksumCheckingResult();

            // Progress tracking
            AtomicInteger totalTasks = new AtomicInteger(officialChecksums.size());
            AtomicInteger completedTasks = new AtomicInteger(0);
            callback.sendProgress(0.0);



            // Parallel checksum generation
            for (Checksum official : officialChecksums) {
                futureChecksums.add(ThreadPool.FIXED_DAEMON.submit(() -> {
                    Checksum generated = official
                        .getAlgorithm()
                        .generateChecksum(inputSource::updateMessageDigest);



                    double progress = completedTasks.incrementAndGet() / totalTasks.doubleValue();
                    callback.sendProgress(progress);



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

            callback.sendProgress(1.0);
            callback.sendResult(result);
        } catch (ExecutionException | InterruptedException e) {
            callback.sendException(e);
        }
    }
}
