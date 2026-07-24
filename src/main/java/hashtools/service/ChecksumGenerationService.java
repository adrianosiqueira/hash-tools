package hashtools.service;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.domain.container.ChecksumGenerationContainer;
import hashtools.domain.context.ChecksumGenerationContext;
import hashtools.domain.result.ChecksumGenerationResult;
import hashtools.strategy.checksumgeneratorupdater.ChecksumGeneratorUpdate;

import java.util.Collection;

public class ChecksumGenerationService {

    private ChecksumGenerationContext context;



    public ChecksumGenerationContainer generateChecksums(ChecksumGenerationContext context) {
        this.context = context;



        // Problem detection
        String problem = context
            .detectProblem()
            .orElse(null);

        if (problem != null) {
            return ChecksumGenerationContainer.problem(problem);
        }



        // Data getting
        Collection<ChecksumGenerator> generators = context
            .getAlgorithms()
            .stream()
            .map(ChecksumGenerator::createFromAlgorithm)
            .toList();



        // Processing
        ChecksumGeneratorUpdate.Result updateResult = context.updateChecksumGenerators(generators);

        if (updateResult instanceof ChecksumGeneratorUpdate.Result.Failure(Exception exception)) {
            return ChecksumGenerationContainer.exception(exception);
        }



        // Result collecting
        ChecksumGenerationResult result = new ChecksumGenerationResult();
        result.setIdentification(context::getIdentification);

        generators
            .stream()
            .map(ChecksumGenerator::decodeIntoChecksum)
            .forEach(result::addChecksum);

        return ChecksumGenerationContainer.result(result);
    }

    public void cancelChecksumGeneration() {
        context.cancelChecksumGeneratorsUpdate();
    }
}
