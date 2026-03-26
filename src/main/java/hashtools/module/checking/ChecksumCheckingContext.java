package hashtools.module.checking;

import hashtools.core.engine.ChecksumGenerator;
import hashtools.core.model.Algorithm;
import hashtools.core.model.Checksum;
import hashtools.core.strategy.checksumextractor.ChecksumExtractor;
import hashtools.core.strategy.checksumidentifier.ChecksumIdentifier;
import hashtools.core.strategy.messagedigest.MessageDigestUpdater;

import java.util.List;
import java.util.Optional;

public class ChecksumCheckingContext {

    private ChecksumExtractor extractor;
    private MessageDigestUpdater updater;
    private ChecksumIdentifier identifier;
    private ChecksumGenerator generator;



    public ChecksumCheckingContext() {
        this.extractor = ChecksumExtractor.nullImplementation();
        this.updater = MessageDigestUpdater.nullImplementation();
        this.identifier = ChecksumIdentifier.nullImplementation();
        this.generator = new ChecksumGenerator();
    }



    public void setExtractor(ChecksumExtractor extractor) {
        this.extractor = Optional
            .ofNullable(extractor)
            .orElseGet(ChecksumExtractor::nullImplementation);
    }

    public void setUpdater(MessageDigestUpdater updater) {
        this.updater = Optional
            .ofNullable(updater)
            .orElseGet(MessageDigestUpdater::nullImplementation);
    }

    public void setIdentifier(ChecksumIdentifier identifier) {
        this.identifier = Optional
            .ofNullable(identifier)
            .orElseGet(ChecksumIdentifier::nullImplementation);
    }



    public List<Checksum> extractOfficialChecksums() {
        return extractor.extract();
    }

    public String getIdentification() {
        return identifier.getIdentification();
    }

    public Checksum generateChecksum(Algorithm algorithm) {
        return generator.generate(algorithm, updater);
    }
}
