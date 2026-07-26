package hashtools.domain.context;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.strategy.algorithmsource.AlgorithmSource;
import hashtools.strategy.checksumgeneratorupdater.ChecksumGeneratorUpdate;
import hashtools.strategy.inputidentification.InputIdentification;
import hashtools.strategy.problemdetection.ProblemDetection;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class ChecksumComparisonContext {

    private InputIdentification inputIdentification1;
    private ChecksumGeneratorUpdate checksumGeneratorUpdate1;
    private ProblemDetection problemDetection1;

    private InputIdentification inputIdentification2;
    private ChecksumGeneratorUpdate checksumGeneratorUpdate2;
    private ProblemDetection problemDetection2;

    private AlgorithmSource algorithmSource;



    public ChecksumComparisonContext() {
        this.inputIdentification1 = new InputIdentification() {};
        this.checksumGeneratorUpdate1 = new ChecksumGeneratorUpdate() {};
        this.problemDetection1 = new ProblemDetection() {};

        this.inputIdentification2 = new InputIdentification() {};
        this.checksumGeneratorUpdate2 = new ChecksumGeneratorUpdate() {};
        this.problemDetection2 = new ProblemDetection() {};

        this.algorithmSource = new AlgorithmSource() {};
    }



    public String getIdentification1() {
        return inputIdentification1.getIdentification();
    }

    public ChecksumGeneratorUpdate.Result updateChecksumGenerators1(Collection<ChecksumGenerator> generators) {
        return checksumGeneratorUpdate1.updateChecksumGenerators(generators);
    }

    public String getIdentification2() {
        return inputIdentification2.getIdentification();
    }

    public ChecksumGeneratorUpdate.Result updateChecksumGenerators2(Collection<ChecksumGenerator> generators) {
        return checksumGeneratorUpdate2.updateChecksumGenerators(generators);
    }

    public void cancelChecksumGeneratorsUpdate() {
        checksumGeneratorUpdate1.cancelChecksumGeneratorsUpdate();
        checksumGeneratorUpdate2.cancelChecksumGeneratorsUpdate();
    }

    public Optional<String> detectProblem() {
        return problemDetection1
            .detectProblem()
            .or(problemDetection2::detectProblem);
    }

    public Collection<ChecksumGenerator> createChecksumGenerators() {
        return algorithmSource
            .getAlgorithms()
            .stream()
            .map(ChecksumGenerator::createFromAlgorithm)
            .toList();
    }



    public void setInputIdentification1(InputIdentification inputIdentification1) {
        this.inputIdentification1 = Objects.requireNonNull(inputIdentification1);
    }

    public void setChecksumGeneratorUpdate1(ChecksumGeneratorUpdate checksumGeneratorUpdate1) {
        this.checksumGeneratorUpdate1 = Objects.requireNonNull(checksumGeneratorUpdate1);
    }

    public void setProblemDetection1(ProblemDetection problemDetection1) {
        this.problemDetection1 = Objects.requireNonNull(problemDetection1);
    }

    public void setInputIdentification2(InputIdentification inputIdentification2) {
        this.inputIdentification2 = Objects.requireNonNull(inputIdentification2);
    }

    public void setChecksumGeneratorUpdate2(ChecksumGeneratorUpdate checksumGeneratorUpdate2) {
        this.checksumGeneratorUpdate2 = Objects.requireNonNull(checksumGeneratorUpdate2);
    }

    public void setProblemDetection2(ProblemDetection problemDetection2) {
        this.problemDetection2 = Objects.requireNonNull(problemDetection2);
    }

    public void setAlgorithmSource(AlgorithmSource algorithmSource) {
        this.algorithmSource = algorithmSource;
    }
}
