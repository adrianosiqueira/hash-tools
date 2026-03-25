package hashtools.module.generation;

import hashtools.core.engine.ChecksumGenerator;
import hashtools.core.model.Algorithm;
import hashtools.core.model.Checksum;
import hashtools.core.strategy.checksumidentifier.ChecksumIdentifier;
import hashtools.core.strategy.checksumidentifier.NullChecksumIdentifier;
import hashtools.core.strategy.messagedigest.MessageDigestUpdater;
import hashtools.core.strategy.messagedigest.NullMessageDigestUpdater;

import java.util.ArrayList;
import java.util.List;

public class ChecksumGenerationContext {

    private List<Algorithm> algorithms;
    private MessageDigestUpdater updater;
    private ChecksumIdentifier identifier;
    private ChecksumGenerator generator;



    public ChecksumGenerationContext() {
        this.algorithms = new ArrayList<>();
        this.updater = new NullMessageDigestUpdater();
        this.identifier = new NullChecksumIdentifier();
        this.generator = new ChecksumGenerator();
    }



    public void setAlgorithms(List<Algorithm> algorithms) {
        this.algorithms = algorithms;
    }

    public void setUpdater(MessageDigestUpdater updater) {
        this.updater = updater;
    }

    public void setIdentifier(ChecksumIdentifier identifier) {
        this.identifier = identifier;
    }



    public List<Algorithm> getAlgorithms() {
        return algorithms;
    }

    public String getIdentification() {
        return identifier.getIdentification();
    }

    public Checksum generateChecksum(Algorithm algorithm) {
        return generator.generate(algorithm, updater);
    }
}
