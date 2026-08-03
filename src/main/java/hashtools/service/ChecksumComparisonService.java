package hashtools.service;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.domain.checksum.Checksum;
import hashtools.domain.checksum.ComparatorChecksum;
import hashtools.domain.context.ChecksumComparisonContext;
import hashtools.domain.result.ChecksumComparisonResult;
import hashtools.domain.result.ExceptionResult;
import hashtools.domain.result.ProblemResult;
import hashtools.strategy.checksumgeneratorupdater.ChecksumGeneratorUpdate;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
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



        // Processing
        Future<Checksum> futureChecksum1 = this.generateChecksumAsync(context::updateChecksumGenerators1);
        Future<Checksum> futureChecksum2 = this.generateChecksumAsync(context::updateChecksumGenerators2);



        // Result getting
        try {
            ComparatorChecksum checksum = new ComparatorChecksum();
            checksum.setChecksum1(futureChecksum1.get());
            checksum.setChecksum2(futureChecksum2.get());

            ChecksumComparisonResult result = new ChecksumComparisonResult();
            result.setChecksum(checksum);

            return result;
        } catch (Exception e) {
            return new ExceptionResult(e);
        }
    }

    public void cancelChecksumsComparison() {
        context.cancelChecksumGeneratorsUpdate();
    }



    private Future<Checksum> generateChecksumAsync(Function<Collection<ChecksumGenerator>, ChecksumGeneratorUpdate.Result> updater) {
        return CompletableFuture.supplyAsync(() -> {
            Collection<ChecksumGenerator> generators = context.createChecksumGenerators();



            ChecksumGeneratorUpdate.Result updateResult = updater.apply(generators);

            if (updateResult instanceof ExceptionResult result) {
                result.throwAsRuntimeException();
            }



            return generators
                .iterator()
                .next()
                .decodeIntoChecksum();
        });
    }



    public sealed interface Result permits ExceptionResult, ProblemResult, ChecksumComparisonResult {}
}
