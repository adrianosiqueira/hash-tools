package hashtools.service;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.domain.checksum.CheckerChecksum;
import hashtools.domain.checksum.Checksum;
import hashtools.domain.context.ChecksumCheckingContext;
import hashtools.domain.result.CanceledResult;
import hashtools.domain.result.ChecksumCheckingResult;
import hashtools.domain.result.ExceptionResult;
import hashtools.domain.result.ProblemResult;
import hashtools.strategy.checksumsource.ChecksumSource;
import hashtools.strategy.inputsource.InputSource;

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
        ChecksumSource.Result extractionResult = context.extractOfficialChecksums();

        switch (extractionResult) {
            case CanceledResult result -> {
                return result;
            }
            case ExceptionResult result -> {
                return result;
            }
            default -> {}
        }



        Collection<ChecksumWithGeneratorMap> checksumsMap = ((ChecksumSource.SuccessResult) extractionResult)
            .getChecksumsStream()
            .map(ChecksumWithGeneratorMap::createFromChecksum)
            .toList();

        List<ChecksumGenerator> generators = checksumsMap
            .stream()
            .map(ChecksumWithGeneratorMap::getChecksumGenerator)
            .toList();



        // Processing
        InputSource.Result updateResult = context.updateChecksumGenerators(generators);

        switch (updateResult) {
            case CanceledResult result -> {
                return result;
            }
            case ExceptionResult result -> {
                return result;
            }
            default -> {}
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
        context.cancelChecksumExtraction();
    }



    public sealed interface Result permits CanceledResult, ExceptionResult, ProblemResult, ChecksumCheckingResult {}



    private static class ChecksumWithGeneratorMap {

        private Checksum checksum;
        private ChecksumGenerator generator;



        private ChecksumWithGeneratorMap() {
        }



        public static ChecksumWithGeneratorMap createFromChecksum(Checksum checksum) {
            ChecksumWithGeneratorMap map = new ChecksumWithGeneratorMap();
            map.checksum = checksum;
            map.generator = ChecksumGenerator.createFromChecksum(checksum);

            return map;
        }



        public CheckerChecksum decodeIntoCheckerChecksum() {
            CheckerChecksum checkerChecksum = new CheckerChecksum();
            checkerChecksum.setOfficial(checksum);
            checkerChecksum.setGenerated(generator.decodeIntoChecksum());
            return checkerChecksum;
        }

        public ChecksumGenerator getChecksumGenerator() {
            return generator;
        }
    }
}
