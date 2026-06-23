package hashtools.module.generator.domain;

import hashtools.core.checksum.Algorithm;
import hashtools.core.source.InputSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChecksumGenerationParameter {

    private InputSource inputSource;
    private List<Algorithm> algorithms;



    public ChecksumGenerationParameter() {
        this.inputSource = InputSource.nullInputSource();
        this.algorithms = new ArrayList<>();
    }



    public InputSource getInputSource() {
        return inputSource;
    }

    public void setInputSource(InputSource inputSource) {
        this.inputSource = Objects.requireNonNull(inputSource);
    }

    public List<Algorithm> getAlgorithms() {
        return algorithms;
    }

    public void setAlgorithms(List<Algorithm> algorithms) {
        this.algorithms = Objects.requireNonNull(algorithms);
    }
}
