package hashtools.backend.generator.service;

import hashtools.core.checksum.Algorithm;
import hashtools.core.checksum.Checksum;
import hashtools.core.source.AlgorithmSource;
import hashtools.core.source.InputSource;
import hashtools.core.threadpool.ThreadPool;
import hashtools.backend.generator.domain.ChecksumGenerationContainer;
import hashtools.backend.generator.domain.ChecksumGenerationParameter;
import hashtools.backend.generator.domain.ChecksumGenerationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class GeneratorService {

    public ChecksumGenerationContainer performChecksumGeneration(ChecksumGenerationParameter parameter) {
        // Initial setup
        parameter.updateProgress(0.0);

        InputSource inputSource = parameter.getInputSource();
        AlgorithmSource algorithmSource = parameter.getAlgorithmSource();



        // Problem detection
        String problem = inputSource
            .detectProblem()
            .or(algorithmSource::detectProblem)
            .orElse(null);

        if (problem != null) {
            return ChecksumGenerationContainer.problem(problem);
        }



        try {
            // Processing data
            List<Algorithm> algorithms = algorithmSource.getAlgorithms();
            List<Future<Checksum>> futureChecksums = new ArrayList<>();
            ChecksumGenerationResult result = new ChecksumGenerationResult();

            // Progress tracking
            AtomicInteger totalTasks = new AtomicInteger(algorithms.size());
            AtomicInteger completedTasks = new AtomicInteger(0);



            // Parallel checksum generation
            for (Algorithm algorithm : algorithms) {
                futureChecksums.add(ThreadPool.FIXED_DAEMON.submit(() -> {
                    Checksum checksum = algorithm.generateChecksum(inputSource::updateMessageDigest);



                    synchronized (completedTasks) {
                        double progress = completedTasks.incrementAndGet() / totalTasks.doubleValue();
                        parameter.updateProgress(progress);
                    }



                    return checksum;
                }));
            }



            // Result collecting
            for (Future<Checksum> checksum : futureChecksums) {
                result.addChecksum(checksum.get());
            }

            result.setIdentification(inputSource::identify);

            parameter.updateProgress(1.0);
            return ChecksumGenerationContainer.result(result);
        } catch (ExecutionException | InterruptedException e) {
            return ChecksumGenerationContainer.exception(e);
        }
    }
}
