package hashtools.domain.context;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.strategy.checksumsource.ChecksumSource;
import hashtools.strategy.inputsource.InputSource;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class ChecksumCheckingContext {

    private InputSource inputSource;
    private ChecksumSource checksumSource;



    public ChecksumCheckingContext() {
        this.inputSource = new InputSource() {};
        this.checksumSource = new ChecksumSource() {};
    }



    public InputSource.Result updateChecksumGenerators(Collection<ChecksumGenerator> generators) {
        return inputSource.updateChecksumGenerators(generators);
    }

    public void cancelChecksumGeneratorsUpdate() {
        inputSource.cancelChecksumGeneratorsUpdate();
    }

    public Optional<String> detectProblem() {
        return inputSource
            .detectProblem()
            .or(checksumSource::detectProblem);
    }

    public ChecksumSource.Result extractOfficialChecksums() {
        return checksumSource.extractOfficialChecksums();
    }

    public void cancelChecksumExtraction() {
        checksumSource.cancelChecksumsExtraction();
    }

    public void setInputSource(InputSource inputSource) {
        this.inputSource = Objects.requireNonNull(inputSource);
    }

    public void setChecksumSource(ChecksumSource checksumSource) {
        this.checksumSource = Objects.requireNonNull(checksumSource);
    }
}
