package hashtools.module.generation;

import hashtools.core.model.Checksum;
import hashtools.core.strategy.checksumidentifier.ChecksumIdentifier;
import hashtools.core.strategy.checksumidentifier.NullChecksumIdentifier;

public class ChecksumGenerationDTO {

    private ChecksumIdentifier identifier;
    private Checksum checksum;



    public ChecksumGenerationDTO() {
        this.identifier = new NullChecksumIdentifier();
        this.checksum = new Checksum();
    }



    public void setIdentifier(ChecksumIdentifier identifier) {
        this.identifier = identifier;
    }

    public void setChecksum(Checksum checksum) {
        this.checksum = checksum;
    }



    public String getIdentification() {
        return identifier.getIdentification();
    }

    public String getHash() {
        return checksum.getHash();
    }
}
