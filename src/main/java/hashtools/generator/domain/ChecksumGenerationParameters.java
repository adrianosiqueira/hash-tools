package hashtools.generator.domain;

import hashtools.core.domain.Algorithm;
import hashtools.core.domain.InputSource;

import java.util.Collection;

public class ChecksumGenerationParameters {

    private InputSource inputSource;
    private Collection<Algorithm> algorithms;



    public InputSource getInputSource() {
        return inputSource;
    }

    public void setInputSource(InputSource inputSource) {
        this.inputSource = inputSource;
    }

    public Collection<Algorithm> getAlgorithms() {
        return algorithms;
    }

    public void setAlgorithms(Collection<Algorithm> algorithms) {
        this.algorithms = algorithms;
    }
}
