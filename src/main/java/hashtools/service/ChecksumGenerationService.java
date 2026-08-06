package hashtools.service;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.domain.context.ChecksumGenerationContext;
import hashtools.domain.result.CanceledResult;
import hashtools.domain.result.ChecksumGenerationResult;
import hashtools.domain.result.ExceptionResult;
import hashtools.domain.result.ProblemResult;
import hashtools.strategy.inputsource.InputSource;

import java.util.Collection;
import java.util.Optional;

public class ChecksumGenerationService {

    private ChecksumGenerationContext context;



    public Result generateChecksums(ChecksumGenerationContext context) {
        this.context = context;



        // Problem detection
        String problem = context
            .detectProblem()
            .orElse(null);

        if (problem != null) {
            return new ProblemResult(problem);
        }



        // Data getting
        Collection<ChecksumGenerator> generators = context.createChecksumGenerators();



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
        ChecksumGenerationResult result = new ChecksumGenerationResult();
        result.setIdentification(context::getIdentification);

        generators
            .stream()
            .map(ChecksumGenerator::decodeIntoChecksum)
            .forEach(result::addChecksum);

        return result;
    }

    public void cancelChecksumGeneration() {
        Optional
            .ofNullable(context)
            .ifPresent(ChecksumGenerationContext::cancelChecksumGeneratorsUpdate);
    }



    public sealed interface Result permits CanceledResult, ExceptionResult, ProblemResult, ChecksumGenerationResult {}
}
