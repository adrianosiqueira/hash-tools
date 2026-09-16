package hashtools.service;

import hashtools.domain.checksum.ChecksumGenerator;
import hashtools.domain.commom.Result;
import hashtools.domain.parameter.ChecksumGenerationParameter;
import hashtools.domain.result.ChecksumGenerationResult;

import java.util.ArrayList;

public class ChecksumGenerationService {

    public Result<ChecksumGenerationResult, String> generateChecksums(ChecksumGenerationParameter parameter) {
        // Problem detection
        var problem = parameter
            .getInputProblemDetection()
            .detect();

        if (problem.isPresent()) {
            var error = problem.get();
            return new Result.Error<>(error);
        }



        // Data getting
        var generators = new ArrayList<ChecksumGenerator>();

        for (var algorithm : parameter.getAlgorithms()) {
            var generator = ChecksumGenerator.createFromAlgorithm(algorithm);
            generators.add(generator);
        }



        // Processing
        var generatorUpdate = parameter.getGeneratorUpdate();
        var progressTracker = parameter.getProgressTracker();

        var updateResult = generatorUpdate.updateGenerators(generators, progressTracker);

        if (updateResult.isError()) {
            var error = updateResult.getError();
            return new Result.Error<>(error);
        }



        // Result collecting
        var identification = parameter
            .getIdentification()
            .identify();

        var checksumGenerationResult = new ChecksumGenerationResult();
        checksumGenerationResult.setIdentification(identification);

        for (var generator : generators) {
            var checksum = generator.decodeIntoChecksum();
            checksumGenerationResult.addChecksum(checksum);
        }

        return new Result.Ok<>(checksumGenerationResult);
    }
}
