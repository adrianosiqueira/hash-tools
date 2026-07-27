package hashtools.service;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.domain.checksum.CheckerChecksum;
import hashtools.domain.checksum.Checksum;
import hashtools.domain.context.ChecksumCheckingContext;
import hashtools.domain.result.ChecksumCheckingResult;
import hashtools.domain.result.ExceptionResult;
import hashtools.domain.result.ProblemResult;
import hashtools.strategy.checksumextraction.ChecksumExtraction;
import hashtools.strategy.checksumgeneratorupdater.ChecksumGeneratorUpdate;

import java.util.Collection;
import java.util.List;

public class ChecksumCheckingService {

    private ChecksumCheckingContext context;


    public Result checkChecksums(ChecksumCheckingContext context) {
        this.context = context;



        // Problem detection
        String problem = context
            .detectProblem()
            .orElse(null);

        if (problem != null) {
            return new ProblemResult(problem);
        }



        // Data getting
        ChecksumExtraction.Result extractionResult = context.extractOfficialChecksums();

        if (extractionResult instanceof ChecksumExtraction.Result.Failure(Exception exception)) {
            return new ExceptionResult(exception);
        }

        Collection<ChecksumWithGeneratorMap> checksumsMap = ((ChecksumExtraction.Result.Success) extractionResult)
            .checksums()
            .stream()
            .map(ChecksumWithGeneratorMap::createFromChecksum)
            .toList();

        List<ChecksumGenerator> generators = checksumsMap
            .stream()
            .map(ChecksumWithGeneratorMap::generator)
            .toList();



        // Processing
        ChecksumGeneratorUpdate.Result updateResult = context.updateChecksumGenerators(generators);

        if (updateResult instanceof ChecksumGeneratorUpdate.Result.Failure(Exception exception)) {
            return new ExceptionResult(exception);
        }



        // Result collecting
        ChecksumCheckingResult result = new ChecksumCheckingResult();

        checksumsMap
            .stream()
            .map(ChecksumWithGeneratorMap::decodeIntoCheckerChecksum)
            .forEach(result::addChecksum);

        return result;
    }

    public void cancelChecksumChecking() {
        context.cancelChecksumGeneratorsUpdate();
    }



    public sealed interface Result permits ExceptionResult, ProblemResult, ChecksumCheckingResult {}



    public record ChecksumWithGeneratorMap(
        Checksum checksum,
        ChecksumGenerator generator
    ) {

        public static ChecksumWithGeneratorMap createFromChecksum(Checksum checksum) {
            return new ChecksumWithGeneratorMap(
                checksum,
                ChecksumGenerator.createFromChecksum(checksum)
            );
        }



        public CheckerChecksum decodeIntoCheckerChecksum() {
            CheckerChecksum checkerChecksum = new CheckerChecksum();
            checkerChecksum.setOfficial(checksum);
            checkerChecksum.setGenerated(generator.decodeIntoChecksum());
            return checkerChecksum;
        }
    }
}
