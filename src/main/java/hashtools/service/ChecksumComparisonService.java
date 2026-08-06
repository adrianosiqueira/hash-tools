package hashtools.service;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.domain.checksum.Checksum;
import hashtools.domain.checksum.ComparatorChecksum;
import hashtools.domain.context.ChecksumComparisonContext;
import hashtools.domain.result.CanceledResult;
import hashtools.domain.result.ChecksumComparisonResult;
import hashtools.domain.result.ExceptionResult;
import hashtools.domain.result.ProblemResult;
import hashtools.strategy.inputsource.InputSource;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

public class ChecksumComparisonService {

    private ChecksumComparisonContext context;



    public Result compareChecksums(ChecksumComparisonContext context) {
        this.context = context;



        // Problem detection
        String problem = context
            .detectProblem()
            .orElse(null);

        if (problem != null) {
            return new ProblemResult(problem);
        }



        // Data getting
        Collection<ChecksumGenerator> generators1 = context.createChecksumGenerators();
        Collection<ChecksumGenerator> generators2 = context.createChecksumGenerators();



        // Processing
        Future<InputSource.Result> futureResult1 = CompletableFuture.supplyAsync(() -> context.updateChecksumGenerators1(generators1));
        Future<InputSource.Result> futureResult2 = CompletableFuture.supplyAsync(() -> context.updateChecksumGenerators1(generators2));



        // Result getting
        try {
            switch (futureResult1.get()) {
                case CanceledResult result -> {
                    return result;
                }
                case ExceptionResult result -> {
                    return result;
                }
                default -> {}
            }

            switch (futureResult2.get()) {
                case CanceledResult result -> {
                    return result;
                }
                case ExceptionResult result -> {
                    return result;
                }
                default -> {}
            }
        } catch (Exception e) {
            return new ExceptionResult(e);
        }

        ComparatorChecksum checksum = new ComparatorChecksum();
        this.consumeChecksumFromGenerator(generators1, checksum::setChecksum1);
        this.consumeChecksumFromGenerator(generators2, checksum::setChecksum2);

        ChecksumComparisonResult result = new ChecksumComparisonResult();
        result.setChecksum(checksum);

        return result;
    }

    public void cancelChecksumsComparison() {
        context.cancelChecksumGeneratorsUpdate();
    }



    private Future<Checksum> generateChecksumAsync(Function<Collection<ChecksumGenerator>, InputSource.Result> updater) {
        return CompletableFuture.supplyAsync(() -> {
            Collection<ChecksumGenerator> generators = context.createChecksumGenerators();



            InputSource.Result updateResult = updater.apply(generators);

            if (updateResult instanceof ExceptionResult result) {
                result.throwAsRuntimeException();
            }



            return generators
                .iterator()
                .next()
                .decodeIntoChecksum();
        });
    }

    private void consumeChecksumFromGenerator(Collection<ChecksumGenerator> generators, Consumer<Checksum> consumer) {
        Checksum checksum = generators
            .iterator()
            .next()
            .decodeIntoChecksum();

        consumer.accept(checksum);
    }



    public sealed interface Result permits CanceledResult, ExceptionResult, ProblemResult, ChecksumComparisonResult {}
}
