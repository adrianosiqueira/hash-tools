package hashtools.module.generation;

import hashtools.core.model.Checksum;
import hashtools.core.strategy.checksumidentifier.ChecksumIdentifier;

import java.util.Optional;

public class ChecksumGenerationDTO {

    private ChecksumIdentifier identifier;
    private Checksum checksum;



    public ChecksumGenerationDTO() {
        this.identifier = ChecksumIdentifier.nullImplementation();
        this.checksum = Checksum.empty();
    }



    public void setIdentifier(ChecksumIdentifier identifier) {
        this.identifier = Optional
            .ofNullable(identifier)
            .orElseGet(ChecksumIdentifier::nullImplementation);
    }

    public void setChecksum(Checksum checksum) {
        this.checksum = Optional
            .ofNullable(checksum)
            .orElseGet(Checksum::empty);
    }



    public String getIdentification() {
        return identifier.getIdentification();
    }

    public String getHash() {
        return checksum.getHash();
    }
}
