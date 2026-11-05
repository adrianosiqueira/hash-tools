package hashtools.comparator.domain;

import hashtools.core.domain.Algorithm;
import hashtools.core.domain.InputSource;

import java.util.Objects;

public class ChecksumComparisonParameters {

    private InputSource inputSource1;
    private InputSource inputSource2;
    private Algorithm algorithm;



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
}
