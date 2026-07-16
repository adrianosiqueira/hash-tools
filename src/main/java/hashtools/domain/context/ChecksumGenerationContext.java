package hashtools.domain.context;

import hashtools.domain.algorithm.Algorithm;
import hashtools.strategy.algorithmsource.AlgorithmSource;
import hashtools.strategy.inputsource.InputSource;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class ChecksumGenerationContext {

    private InputSource inputSource;
    private AlgorithmSource algorithmSource;
    private Consumer<Double> progressConsumer;



    public ChecksumGenerationContext() {
        this.inputSource = new InputSource() {};
        this.algorithmSource = new AlgorithmSource() {};
        this.progressConsumer = _ -> {};
    }



    public Optional<String> detectProblem() {
        return inputSource
            .detectProblem()
            .or(algorithmSource::detectProblem);
    }

    public String getIdentification() {
        return inputSource.getIdentification();
    }

    public void updateMessageDigests(Collection<MessageDigest> messageDigests) throws IOException {
        inputSource.updateMessageDigest(messageDigests);
    }

    public Collection<Algorithm> getAlgorithms() {
        return algorithmSource.getAlgorithms();
    }

    public void updateProgress(double progress) {
        progressConsumer.accept(progress);
    }



    public void setInputSource(InputSource inputSource) {
        this.inputSource = Objects.requireNonNull(inputSource);
    }

    public void setAlgorithmSource(AlgorithmSource algorithmSource) {
        this.algorithmSource = algorithmSource;
    }

    public void setProgressConsumer(Consumer<Double> progressConsumer) {
        this.progressConsumer = Objects.requireNonNull(progressConsumer);
    }
}
