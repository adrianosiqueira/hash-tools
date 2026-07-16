package hashtools.domain.context;

import hashtools.strategy.algorithmsource.AlgorithmSource;
import hashtools.strategy.inputsource.InputSource;
import hashtools.strategy.algorithmsource.NullAlgorithmSource;
import hashtools.strategy.inputsource.NullInputSource;

import java.util.Objects;
import java.util.function.Consumer;

public class ChecksumGenerationParameter {

    private InputSource inputSource;
    private AlgorithmSource algorithmSource;
    private Consumer<Double> progressConsumer;



    public ChecksumGenerationParameter() {
        this.inputSource = new NullInputSource();
        this.algorithmSource = new NullAlgorithmSource();
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
