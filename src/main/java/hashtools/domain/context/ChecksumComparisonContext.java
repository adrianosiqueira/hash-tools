package hashtools.domain.context;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.strategy.algorithmsource.AlgorithmSource;
import hashtools.strategy.inputsource.InputSource;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class ChecksumComparisonContext {

    private InputSource inputSource1;
    private InputSource inputSource2;
    private AlgorithmSource algorithmSource;



    public ChecksumComparisonContext() {
        this.inputSource1 = new InputSource() {};
        this.inputSource2 = new InputSource() {};
        this.algorithmSource = new AlgorithmSource() {};
    }



    public InputSource.Result updateChecksumGenerators1(Collection<ChecksumGenerator> generators) {
        return inputSource1.updateChecksumGenerators(generators);
    }

    public InputSource.Result updateChecksumGenerators2(Collection<ChecksumGenerator> generators) {
        return inputSource2.updateChecksumGenerators(generators);
    }

    public void cancelChecksumGeneratorsUpdate() {
        inputSource1.cancelChecksumGeneratorsUpdate();
        inputSource2.cancelChecksumGeneratorsUpdate();
    }

    public Optional<String> detectProblem() {
        return inputSource1
            .detectProblem()
            .or(inputSource2::detectProblem)
            .or(algorithmSource::detectProblem);
    }

    public Collection<ChecksumGenerator> createChecksumGenerators() {
        return algorithmSource
            .getAlgorithms()
            .stream()
            .map(ChecksumGenerator::createFromAlgorithm)
            .toList();
    }

    public void setInputSource1(InputSource inputSource1) {
        this.inputSource1 = Objects.requireNonNull(inputSource1);
    }

    public void setInputSource2(InputSource inputSource2) {
        this.inputSource2 = Objects.requireNonNull(inputSource2);
    }

    public void setAlgorithmSource(AlgorithmSource algorithmSource) {
        this.algorithmSource = Objects.requireNonNull(algorithmSource);
    }
}
