package hashtools.service;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.domain.checksum.Checksum;
import hashtools.domain.checksum.ChecksumPair;
import hashtools.domain.commom.Result;
import hashtools.domain.parameter.ChecksumCheckingParameter;
import hashtools.domain.result.ChecksumCheckingResult;

import java.util.HashMap;

public class ChecksumCheckingService {

    public Result<ChecksumCheckingResult, String> checkChecksums(ChecksumCheckingParameter parameter) {
        // Problem detection
        var problem = parameter
            .getInputProblemDetection()
            .detect()
            .or(parameter.getChecksumProblemDetection()::detect);

        if (problem.isPresent()) {
            var error = problem.get();
            return new Result.Error<>(error);
        }



        // Data getting
        var extractionResult = parameter
            .getChecksumExtraction()
            .extractChecksums();

        if (extractionResult.isError()) {
            var error = extractionResult.getError();
            return new Result.Error<>(error);
        }

        var officialChecksums = extractionResult.getValue();
        var checksumGeneratorsMap = new HashMap<Checksum, ChecksumGenerator>();

        for (var officialChecksum : officialChecksums) {
            var algorithm = officialChecksum.getAlgorithm();
            var generator = ChecksumGenerator.createFromAlgorithm(algorithm);

            checksumGeneratorsMap.put(officialChecksum, generator);
        }



        // Processing
        var generatorUpdate = parameter.getGeneratorUpdate();
        var progressTracker = parameter.getProgressTracker();
        var generators = checksumGeneratorsMap.values();

        var updateResult = generatorUpdate.updateGenerators(generators, progressTracker);

        if (updateResult.isError()) {
            var error = updateResult.getError();
            return new Result.Error<>(error);
        }



        // Result collecting
        var checksumCheckingResult = new ChecksumCheckingResult();

        for (var entry : checksumGeneratorsMap.entrySet()) {
            var officialChecksum = entry.getKey();

            var generatedChecksum = entry
                .getValue()
                .decodeIntoChecksum();

            var checksumPair = new ChecksumPair();
            checksumPair.setOfficialChecksum(officialChecksum);
            checksumPair.setGeneratedChecksum(generatedChecksum);

            checksumCheckingResult.addChecksum(checksumPair);
        }

        return new Result.Ok<>(checksumCheckingResult);
    }
}
