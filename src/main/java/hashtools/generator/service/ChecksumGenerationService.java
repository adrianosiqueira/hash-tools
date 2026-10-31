package hashtools.generator.service;

import hashtools.core.domain.ProblemException;
import hashtools.generator.domain.ChecksumGenerationParameters;
import hashtools.generator.domain.GenerationChecksumGenerators;
import hashtools.generator.domain.GenerationChecksums;

import java.io.IOException;

public class ChecksumGenerationService {

    public GenerationChecksums perform(ChecksumGenerationParameters parameters)
    throws InterruptedException, IOException, ProblemException {
        var inputSource = parameters.getInputSource();
        inputSource.detectProblem();



        var checksumGenerators = new GenerationChecksumGenerators();
        checksumGenerators.addFromAlgorithms(parameters.getAlgorithms());
        checksumGenerators.update(inputSource::updateGenerators);



        var generationChecksums = new GenerationChecksums();
        checksumGenerators.decodeAndCollect(generationChecksums::add);

        return generationChecksums;
    }
}
