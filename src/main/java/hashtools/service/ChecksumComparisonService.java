package hashtools.service;

import hashtools.domain.checksum.Algorithm;
import hashtools.domain.checksum.Checksum;
import hashtools.domain.checksum.ChecksumGenerator;
import hashtools.domain.checksum.ChecksumPair;
import hashtools.domain.commom.Result;
import hashtools.domain.parameter.ChecksumComparisonParameter;
import hashtools.domain.result.ChecksumComparisonResult;
import hashtools.strategy.generatorupdate.GeneratorUpdate;
import hashtools.strategy.threadfactory.ThreadFactories;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ChecksumComparisonService {

    public Result<ChecksumComparisonResult, String> compareChecksums(ChecksumComparisonParameter parameter) {
        // Problem detection
        var problem = parameter
            .getInputProblemDetection1()
            .detect()
            .or(parameter.getInputProblemDetection2()::detect);

        if (problem.isPresent()) {
            var error = problem.get();
            return new Result.Error<>(error);
        }



        // Processing
        var futureChecksum1 = new CompletableFuture<Result<Checksum, String>>();
        var futureChecksum2 = new CompletableFuture<Result<Checksum, String>>();

        var threadPool = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors(),
            ThreadFactories::newPlatformDaemon
        );
        threadPool.execute(() -> this.generateChecksum(
            parameter.getGeneratorUpdate1(),
            parameter.getAlgorithm(),
            parameter.getProgressTracker(),
            futureChecksum1::complete
        ));
        threadPool.execute(() -> this.generateChecksum(
            parameter.getGeneratorUpdate2(),
            parameter.getAlgorithm(),
            parameter.getProgressTracker(),
            futureChecksum2::complete
        ));



        // Result getting
        try {
            var generationResult1 = futureChecksum1.get();
            var generationResult2 = futureChecksum2.get();

            if (generationResult1.isError()) {
                var error = generationResult1.getError();
                return new Result.Error<>(error);
            } else if (generationResult2.isError()) {
                var error = generationResult2.getError();
                return new Result.Error<>(error);
            }

            Checksum checksum1 = generationResult1.getValue();
            Checksum checksum2 = generationResult2.getValue();

            var checksumPair = new ChecksumPair();
            checksumPair.setGeneratedChecksum1(checksum1);
            checksumPair.setGeneratedChecksum2(checksum2);

            var checksumComparisonResult = new ChecksumComparisonResult();
            checksumComparisonResult.setChecksum(checksumPair);
            return new Result.Ok<>(checksumComparisonResult);
        } catch (ExecutionException e) {
            var error = e.getMessage();
            return new Result.Error<>(error);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            threadPool.shutdownNow();
            return new Result.Error<>("Checksum comparison canceled");
        } finally {
            /*
             * Do not use try-with-resources because it prevents the current
             * thread from being interrupted.
             */
            threadPool.close();
        }
    }

    private void generateChecksum(GeneratorUpdate generatorUpdate, Algorithm algorithm, Consumer<Double> progressTracker, Consumer<Result<Checksum, String>> resultConsumer) {
        var generator = ChecksumGenerator.createFromAlgorithm(algorithm);
        var updateResult = generatorUpdate.updateGenerators(List.of(generator), progressTracker);

        if (updateResult.isOk()) {
            var checksum = generator.decodeIntoChecksum();
            var result = new Result.Ok<Checksum, String>(checksum);
            resultConsumer.accept(result);
        } else {
            var error = updateResult.getError();
            var result = new Result.Error<Checksum, String>(error);
            resultConsumer.accept(result);
        }
    }
}
