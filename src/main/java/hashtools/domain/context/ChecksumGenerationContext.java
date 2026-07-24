package hashtools.domain.context;

import hashtools.domain.algorithm.Algorithm;
import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.strategy.algorithmsource.AlgorithmSource;
import hashtools.strategy.checksumgeneratorupdater.ChecksumGeneratorUpdate;
import hashtools.strategy.inputidentification.InputIdentification;
import hashtools.strategy.inputsource.InputSource;
import hashtools.strategy.problemdetection.ProblemDetection;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class ChecksumGenerationContext {

    private InputIdentification inputIdentification;
    private ChecksumGeneratorUpdate checksumGeneratorUpdate;
    private ProblemDetection problemDetection;
    private AlgorithmSource algorithmSource;



    public ChecksumGenerationContext() {
        this.inputIdentification = new InputIdentification() {};
        this.checksumGeneratorUpdate = new ChecksumGeneratorUpdate() {};
        this.problemDetection = new ProblemDetection() {};
        this.algorithmSource = new AlgorithmSource() {};
    }



    public String getIdentification() {
        return inputIdentification.getIdentification();
    }

    public ChecksumGeneratorUpdate.Result updateChecksumGenerators(Collection<ChecksumGenerator> checksumGenerators) {
        return checksumGeneratorUpdate.updateChecksumGenerators(checksumGenerators);
    }

    public void cancelChecksumGeneratorsUpdate() {
        checksumGeneratorUpdate.cancelChecksumGeneratorsUpdate();
    }

    public Optional<String> detectProblem() {
        return problemDetection.detectProblem();
    }

    public Collection<Algorithm> getAlgorithms() {
        return algorithmSource.getAlgorithms();
    }



    public void setInputIdentification(InputIdentification inputIdentification) {
        this.inputIdentification = Objects.requireNonNull(inputIdentification);
    }

    public void setChecksumGeneratorUpdate(ChecksumGeneratorUpdate checksumGeneratorUpdate) {
        this.checksumGeneratorUpdate = Objects.requireNonNull(checksumGeneratorUpdate);
    }

    public void setProblemDetection(ProblemDetection problemDetection) {
        this.problemDetection = Objects.requireNonNull(problemDetection);
    }

    public void setAlgorithmSource(AlgorithmSource algorithmSource) {
        this.algorithmSource = Objects.requireNonNull(algorithmSource);
    }



    @Deprecated(forRemoval = true)
    public void updateMessageDigests(Collection<MessageDigest> messageDigests) throws IOException {
    }

    @Deprecated(forRemoval = true)
    public void setInputSource(InputSource inputSource) {
    }

    @Deprecated(forRemoval = true)
    public void setProgressConsumer(Consumer<Double> progressConsumer) {
    }
}
