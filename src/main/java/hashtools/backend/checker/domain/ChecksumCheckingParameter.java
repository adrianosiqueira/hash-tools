package hashtools.backend.checker.domain;

import hashtools.backend.core.interfaces.ChecksumSource;
import hashtools.backend.core.interfaces.InputSource;
import hashtools.backend.core.strategy.checksumsource.NullChecksumSource;
import hashtools.backend.core.strategy.inputsource.NullInputSource;

import java.util.Objects;
import java.util.function.Consumer;

public class ChecksumCheckingParameter {

    private InputSource inputSource;
    private ChecksumSource checksumSource;
    private Consumer<Double> progressConsumer;



    public ChecksumCheckingParameter() {
        this.inputSource = new NullInputSource();
        this.checksumSource = new NullChecksumSource();
        this.progressConsumer = _ -> {};
    }



    public InputSource getInputSource() {
        return inputSource;
    }

    public void setInputSource(InputSource inputSource) {
        this.inputSource = Objects.requireNonNull(inputSource);
    }

    public ChecksumSource getChecksumSource() {
        return checksumSource;
    }

    public void setChecksumSource(ChecksumSource checksumSource) {
        this.checksumSource = Objects.requireNonNull(checksumSource);
    }

    public void updateProgress(double progress) {
        progressConsumer.accept(progress);
    }

    public void setProgressConsumer(Consumer<Double> progressConsumer) {
        this.progressConsumer = Objects.requireNonNull(progressConsumer);
    }
}
