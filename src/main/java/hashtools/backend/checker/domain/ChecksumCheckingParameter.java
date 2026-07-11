package hashtools.backend.checker.domain;

import hashtools.backend.core.source.ChecksumSource;
import hashtools.backend.core.source.InputSource;

import java.util.Objects;
import java.util.function.Consumer;

public class ChecksumCheckingParameter {

    private InputSource inputSource;
    private ChecksumSource checksumSource;
    private Consumer<Double> progressConsumer;



    public ChecksumCheckingParameter() {
        this.inputSource = InputSource.nullInputSource();
        this.checksumSource = ChecksumSource.nullChecksumSource();
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
