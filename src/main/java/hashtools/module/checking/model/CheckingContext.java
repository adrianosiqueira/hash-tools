package hashtools.module.checking.model;

import hashtools.core.engine.ChecksumGenerator;
import hashtools.core.model.Algorithm;
import hashtools.core.model.Checksum;
import hashtools.core.strategy.checksumextractor.ChecksumExtractor;
import hashtools.core.strategy.checksumidentifier.ChecksumIdentifier;
import hashtools.core.strategy.messagedigest.MessageDigestUpdater;

import java.util.List;
import java.util.Optional;

public class CheckingContext {

    private MessageDigestUpdater updater;
    private ChecksumIdentifier identifier;
    private ChecksumExtractor extractor;
    private ChecksumGenerator generator;



    public CheckingContext() {
        this.setUpdater(null);
        this.setIdentifier(null);
        this.setExtractor(null);
        this.setGenerator(null);
    }



    public String getIdentification() {
        return identifier.getIdentification();
    }

    public List<Checksum> extractOfficialChecksums() {
        return extractor.extract();
    }

    public Checksum generateChecksum(Algorithm algorithm) throws Exception {
        return generator.generate(algorithm, updater);
    }



    public MessageDigestUpdater getUpdater() {
        return updater;
    }

    public void setUpdater(MessageDigestUpdater updater) {
        this.updater = Optional
            .ofNullable(updater)
            .orElseGet(MessageDigestUpdater::nullImplementation);
    }

    public ChecksumIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(ChecksumIdentifier identifier) {
        this.identifier = Optional
            .ofNullable(identifier)
            .orElseGet(ChecksumIdentifier::nullImplementation);
    }

    public ChecksumExtractor getExtractor() {
        return extractor;
    }

    public void setExtractor(ChecksumExtractor extractor) {
        this.extractor = Optional
            .ofNullable(extractor)
            .orElseGet(ChecksumExtractor::nullImplementation);
    }

    public void setGenerator(ChecksumGenerator generator) {
        this.generator = Optional
            .ofNullable(generator)
            .orElseGet(ChecksumGenerator::new);
    }
}
