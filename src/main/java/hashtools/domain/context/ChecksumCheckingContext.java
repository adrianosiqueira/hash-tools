package hashtools.domain.context;

import hashtools.domain.checksum.Checksum;
import hashtools.strategy.checksumsource.ChecksumSource;
import hashtools.strategy.inputsource.InputSource;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class ChecksumCheckingContext {

    private InputSource inputSource;
    private ChecksumSource checksumSource;
    private Consumer<Double> progressConsumer;



    public ChecksumCheckingContext() {
        this.inputSource = new InputSource() {};
        this.checksumSource = new ChecksumSource() {};
        this.progressConsumer = _ -> {};
    }



    public Optional<String> detectProblem() {
        return inputSource
            .detectProblem()
            .or(checksumSource::detectProblem);
    }

    public void updateMessageDigests(Collection<MessageDigest> messageDigests) throws IOException {
        inputSource.updateMessageDigest(messageDigests);
    }

    public Collection<Checksum> extractOfficialChecksums() throws IOException {
        return checksumSource.extractOfficialChecksums();
    }

    public void updateProgress(double progress) {
        progressConsumer.accept(progress);
    }



    public void setInputSource(InputSource inputSource) {
        this.inputSource = Objects.requireNonNull(inputSource);
    }

    public void setChecksumSource(ChecksumSource checksumSource) {
        this.checksumSource = Objects.requireNonNull(checksumSource);
    }

    public void setProgressConsumer(Consumer<Double> progressConsumer) {
        this.progressConsumer = Objects.requireNonNull(progressConsumer);
    }
}
