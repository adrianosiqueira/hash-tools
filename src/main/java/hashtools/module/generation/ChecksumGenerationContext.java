package hashtools.module.generation;

import hashtools.core.engine.ChecksumGenerator;
import hashtools.core.model.Algorithm;
import hashtools.core.model.Checksum;
import hashtools.core.strategy.checksumidentifier.ChecksumIdentifier;
import hashtools.core.strategy.messagedigest.MessageDigestUpdater;

import java.util.List;
import java.util.Optional;

public class ChecksumGenerationContext {

    private List<Algorithm> algorithms;
    private MessageDigestUpdater updater;
    private ChecksumIdentifier identifier;
    private ChecksumGenerator generator;



    public ChecksumGenerationContext() {
        this.algorithms = List.of();
        this.updater = MessageDigestUpdater.nullImplementation();
        this.identifier = ChecksumIdentifier.nullImplementation();
        this.generator = new ChecksumGenerator();
    }



    public void setAlgorithms(List<Algorithm> algorithms) {
        this.algorithms = Optional
            .ofNullable(algorithms)
            .orElseGet(List::of);
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
