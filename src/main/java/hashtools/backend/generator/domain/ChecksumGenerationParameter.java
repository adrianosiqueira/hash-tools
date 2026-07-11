package hashtools.backend.generator.domain;

import hashtools.core.source.AlgorithmSource;
import hashtools.core.source.InputSource;

import java.util.Objects;
import java.util.function.Consumer;

public class ChecksumGenerationParameter {

    private InputSource inputSource;
    private AlgorithmSource algorithmSource;
    private Consumer<Double> progressConsumer;



    public ChecksumGenerationParameter() {
        this.inputSource = InputSource.nullInputSource();
        this.algorithmSource = AlgorithmSource.nullAlgorithmSource();
        this.progressConsumer = _ -> {};
    }



    public InputSource getInputSource() {
        return inputSource;
    }

    public void setInputSource(InputSource inputSource) {
        this.inputSource = Objects.requireNonNull(inputSource);
    }

    public AlgorithmSource getAlgorithmSource() {
        return algorithmSource;
    }

    public void setAlgorithmSource(AlgorithmSource algorithmSource) {
        this.algorithmSource = algorithmSource;
    }

    public void updateProgress(double progress) {
        progressConsumer.accept(progress);
    }

    public void setProgressConsumer(Consumer<Double> progressConsumer) {
        this.progressConsumer = Objects.requireNonNull(progressConsumer);
    }
}
