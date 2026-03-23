package hashtools.module.checking;

import hashtools.core.engine.ChecksumGenerator;
import hashtools.core.model.Algorithm;
import hashtools.core.model.Checksum;
import hashtools.core.strategy.checksumextractor.ChecksumExtractor;
import hashtools.core.strategy.checksumextractor.NullChecksumExtractor;
import hashtools.core.strategy.checksumidentifier.ChecksumIdentifier;
import hashtools.core.strategy.checksumidentifier.NullChecksumIdentifier;
import hashtools.core.strategy.messagedigest.MessageDigestUpdater;
import hashtools.core.strategy.messagedigest.NullMessageDigestUpdater;

import java.security.MessageDigest;
import java.util.List;
import java.util.Optional;

public class ChecksumCheckingContext {

    private ChecksumExtractor extractor;
    private MessageDigestUpdater updater;
    private ChecksumIdentifier identifier;
    private ChecksumGenerator generator;



    public ChecksumCheckingContext() {
        this.extractor = new NullChecksumExtractor();
        this.updater = new NullMessageDigestUpdater();
        this.identifier = new NullChecksumIdentifier();
        this.generator = new ChecksumGenerator();
    }



    public void setExtractor(ChecksumExtractor extractor) {
        this.extractor = Optional
            .ofNullable(extractor)
            .orElse(new NullChecksumExtractor());
    }

    public void setUpdater(MessageDigestUpdater updater) {
        this.updater = Optional
            .ofNullable(updater)
            .orElse(new NullMessageDigestUpdater());
    }

    public void setIdentifier(ChecksumIdentifier identifier) {
        this.identifier = Optional
            .ofNullable(identifier)
            .orElse(new NullChecksumIdentifier());
    }



    public List<Checksum> extractOfficialChecksums() {
        return extractor.extract();
    }

    public void updateMessageDigest(MessageDigest messageDigest) {
        updater.update(messageDigest);
    }

    public String getIdentification() {
        return identifier.getIdentification();
    }

    public Checksum generateChecksum(Algorithm algorithm) {
        return generator.generate(algorithm, updater);
    }
}
