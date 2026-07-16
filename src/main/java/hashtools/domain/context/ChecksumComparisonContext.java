package hashtools.domain.context;

import hashtools.domain.algorithm.Algorithm;
import hashtools.strategy.inputsource.InputSource;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class ChecksumComparisonContext {

    private InputSource inputSource1;
    private InputSource inputSource2;
    private Algorithm algorithm;
    private Consumer<Double> progressConsumer;



    public ChecksumComparisonContext() {
        this.inputSource1 = new InputSource() {};
        this.inputSource2 = new InputSource() {};
        this.algorithm = Algorithm.MD5;
        this.progressConsumer = _ -> {};
    }



    public Optional<String> detectProblem() {
        return inputSource1
            .detectProblem()
            .or(inputSource2::detectProblem);
    }

    public String getIdentification1() {
        return inputSource1.getIdentification();
    }

    public void updateMessageDigests1(Collection<MessageDigest> messageDigests) throws IOException {
        inputSource1.updateMessageDigest(messageDigests);
    }

    public String getIdentification2() {
        return inputSource2.getIdentification();
    }

    public void updateMessageDigests2(Collection<MessageDigest> messageDigests) throws IOException {
        inputSource2.updateMessageDigest(messageDigests);
    }

    public void updateProgress(double progress) {
        progressConsumer.accept(progress);
    }



    public InputSource getInputSource1() {
        return inputSource1;
    }

    public void setInputSource1(InputSource inputSource1) {
        this.inputSource1 = Objects.requireNonNull(inputSource1);
    }

    public InputSource getInputSource2() {
        return inputSource2;
    }

    public void setInputSource2(InputSource inputSource2) {
        this.inputSource2 = Objects.requireNonNull(inputSource2);
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = Objects.requireNonNull(algorithm);
    }

    public void setProgressConsumer(Consumer<Double> progressConsumer) {
        this.progressConsumer = Objects.requireNonNull(progressConsumer);
    }
}
