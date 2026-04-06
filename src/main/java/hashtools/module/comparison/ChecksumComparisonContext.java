package hashtools.module.comparison;

import hashtools.core.engine.ChecksumGenerator;
import hashtools.core.event.HashToolsEvent;
import hashtools.core.model.Algorithm;
import hashtools.core.model.Checksum;
import hashtools.core.strategy.checksumidentifier.ChecksumIdentifier;
import hashtools.core.strategy.messagedigest.MessageDigestUpdater;

import java.util.Optional;

public class ChecksumComparisonContext implements HashToolsEvent {

    private Algorithm algorithm;

    private MessageDigestUpdater updater1;
    private ChecksumIdentifier identifier1;

    private MessageDigestUpdater updater2;
    private ChecksumIdentifier identifier2;

    private ChecksumGenerator generator;



    public ChecksumComparisonContext() {
        this.algorithm = Algorithm.getDefault();
        this.updater1 = MessageDigestUpdater.nullImplementation();
        this.identifier1 = ChecksumIdentifier.nullImplementation();
        this.updater2 = MessageDigestUpdater.nullImplementation();
        this.identifier2 = ChecksumIdentifier.nullImplementation();
        this.generator = new ChecksumGenerator();
    }



    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = Optional
            .ofNullable(algorithm)
            .orElseGet(Algorithm::getDefault);
    }

    public void setUpdater1(MessageDigestUpdater updater1) {
        this.updater1 = Optional
            .ofNullable(updater1)
            .orElseGet(MessageDigestUpdater::nullImplementation);
    }

    public void setIdentifier1(ChecksumIdentifier identifier1) {
        this.identifier1 = Optional
            .ofNullable(identifier1)
            .orElseGet(ChecksumIdentifier::nullImplementation);
    }

    public void setUpdater2(MessageDigestUpdater updater2) {
        this.updater2 = Optional
            .ofNullable(updater2)
            .orElseGet(MessageDigestUpdater::nullImplementation);
    }

    public void setIdentifier2(ChecksumIdentifier identifier2) {
        this.identifier2 = Optional
            .ofNullable(identifier2)
            .orElseGet(ChecksumIdentifier::nullImplementation);
    }



    public String getIdentification1() {
        return identifier1.getIdentification();
    }

    public String getIdentification2() {
        return identifier2.getIdentification();
    }

    public Checksum generateChecksum1() {
        return generator.generate(algorithm, updater1);
    }

    public Checksum generateChecksum2() {
        return generator.generate(algorithm, updater2);
    }
}
