package hashtools.module.generator.service;

import hashtools.core.checksum.Algorithm;
import hashtools.core.checksum.Checksum;
import hashtools.core.source.InputSource;
import hashtools.core.threadpool.ThreadPoolFactory;
import hashtools.module.generator.domain.ChecksumGenerationCallback;
import hashtools.module.generator.domain.ChecksumGenerationParameter;
import hashtools.module.generator.domain.ChecksumGenerationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class GeneratorService {

    public void performChecksumGeneration(ChecksumGenerationParameter parameter, ChecksumGenerationCallback callback) {
        InputSource inputSource = parameter.getInputSource();
        List<Algorithm> algorithms = parameter.getAlgorithms();



        // Problem detection
        String problem = inputSource
            .detectProblem()
            .or(() -> algorithms.isEmpty()
                ? Optional.of("There is no algorithms selected")
                : Optional.empty())
            .orElse(null);

        if (problem != null) {
            callback.sendProblem(problem);
            return;
        }



        try (ExecutorService threadPool = ThreadPoolFactory.fixedDaemonPool()) {
            // Processing data
            List<Future<Checksum>> futureChecksums = new ArrayList<>();
            ChecksumGenerationResult result = new ChecksumGenerationResult();

            // Progress tracking
            AtomicInteger totalTasks = new AtomicInteger(algorithms.size());
            AtomicInteger completedTasks = new AtomicInteger(0);
            callback.sendProgress(0.0);



            // Parallel checksum generation
            for (Algorithm algorithm : algorithms) {
                futureChecksums.add(threadPool.submit(() -> {
                    Checksum checksum = algorithm.generateChecksum(inputSource::updateMessageDigest);



                    double progress = completedTasks.incrementAndGet() / totalTasks.doubleValue();
                    callback.sendProgress(progress);



                    return checksum;
                }));
            }



            // Result collecting
            for (Future<Checksum> checksum : futureChecksums) {
                result.addChecksum(checksum.get());
            }

            result.setIdentification(inputSource::identify);

            callback.sendProgress(1.0);
            callback.sendResult(result);
        } catch (ExecutionException | InterruptedException e) {
            callback.sendException(e);
        }
    }
}
