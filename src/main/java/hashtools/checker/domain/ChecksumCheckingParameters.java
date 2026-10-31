package hashtools.checker.domain;

import hashtools.core.domain.InputSource;

public class ChecksumCheckingParameters {

    private InputSource inputSource;
    private ChecksumSource checksumSource;



    public InputSource getInputSource() {
        return inputSource;
    }

    public void setInputSource(InputSource inputSource) {
        this.inputSource = inputSource;
    }

    public ChecksumSource getChecksumSource() {
        return checksumSource;
    }

    public void setChecksumSource(ChecksumSource checksumSource) {
        this.checksumSource = checksumSource;
    }
}
