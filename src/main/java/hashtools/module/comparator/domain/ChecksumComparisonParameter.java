package hashtools.module.comparator.domain;

import hashtools.core.checksum.Algorithm;
import hashtools.core.source.InputSource;

import java.util.Objects;
import java.util.function.Consumer;

public class ChecksumComparisonParameter {

    private InputSource inputSource1;
    private InputSource inputSource2;
    private Algorithm algorithm;
    private Consumer<Double> progressConsumer;



    public ChecksumComparisonParameter() {
        this.inputSource1 = InputSource.nullInputSource();
        this.inputSource2 = InputSource.nullInputSource();
        this.algorithm = Algorithm.MD5;
        this.progressConsumer = _ -> {};
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

    public void updateProgress(double progress) {
        progressConsumer.accept(progress);
    }

    public void setProgressConsumer(Consumer<Double> progressConsumer) {
        this.progressConsumer = Objects.requireNonNull(progressConsumer);
    }
}
