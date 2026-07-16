package hashtools.service;

import hashtools.domain.checksum.Algorithm;
import hashtools.domain.checksum.Checksum;
import hashtools.strategy.algorithmsource.AlgorithmSource;
import hashtools.strategy.inputsource.InputSource;
import hashtools.backend.core.strategy.threadpool.ThreadPool;
import hashtools.backend.core.strategy.threadpool.AllCoreDaemonThreadPool;
import hashtools.domain.container.ChecksumGenerationContainer;
import hashtools.domain.context.ChecksumGenerationParameter;
import hashtools.domain.result.ChecksumGenerationResult;

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



        try (ThreadPool threadPool = new AllCoreDaemonThreadPool()) {
            // Processing data
            List<Algorithm> algorithms = algorithmSource.getAlgorithms();
            List<Future<Checksum>> futureChecksums = new ArrayList<>();
            ChecksumGenerationResult result = new ChecksumGenerationResult();

            // Progress tracking
            AtomicInteger totalTasks = new AtomicInteger(algorithms.size());
            AtomicInteger completedTasks = new AtomicInteger(0);



            // Parallel checksum generation
            for (Algorithm algorithm : algorithms) {
                futureChecksums.add(threadPool.run(() -> {
                    Checksum checksum = algorithm.generateChecksum(inputSource);



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

            result.setIdentification(inputSource::getIdentification);

            parameter.updateProgress(1.0);
            return ChecksumGenerationContainer.result(result);
        } catch (ExecutionException | InterruptedException e) {
            return ChecksumGenerationContainer.exception(e);
        }
    }
}
