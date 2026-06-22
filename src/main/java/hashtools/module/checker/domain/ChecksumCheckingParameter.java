package hashtools.module.checker.domain;

import hashtools.core.source.ChecksumSource;
import hashtools.core.source.InputSource;

import java.util.Objects;

public class ChecksumCheckingParameter {

    private InputSource inputSource;
    private ChecksumSource checksumSource;



    public ChecksumCheckingParameter() {
        this.inputSource = InputSource.nullInputSource();
        this.checksumSource = ChecksumSource.nullChecksumSource();
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
}
