package hashtools.domain.context;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.strategy.algorithmsource.AlgorithmSource;
import hashtools.strategy.inputsource.InputSource;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class ChecksumGenerationContext {

    private InputSource inputSource;
    private AlgorithmSource algorithmSource;



    public ChecksumGenerationContext() {
        this.inputSource = new InputSource() {};
        this.algorithmSource = new AlgorithmSource() {};
    }



    public String getIdentification() {
        return inputSource.getIdentification();
    }

    public InputSource.Result updateChecksumGenerators(Collection<ChecksumGenerator> checksumGenerators) {
        return inputSource.updateChecksumGenerators(checksumGenerators);
    }

    public void cancelChecksumGeneratorsUpdate() {
        inputSource.cancelChecksumGeneratorsUpdate();
    }

    public Optional<String> detectProblem() {
        return inputSource.detectProblem();
    }

    public Collection<ChecksumGenerator> createChecksumGenerators() {
        return algorithmSource
            .getAlgorithms()
            .stream()
            .map(ChecksumGenerator::createFromAlgorithm)
            .toList();
    }

    public void setInputSource(InputSource inputSource) {
        this.inputSource = Objects.requireNonNull(inputSource);
    }

    public void setAlgorithmSource(AlgorithmSource algorithmSource) {
        this.algorithmSource = Objects.requireNonNull(algorithmSource);
    }
}
