package hashtools.module.comparison;

import hashtools.core.model.Checksum;
import hashtools.core.strategy.checksumidentifier.ChecksumIdentifier;

import java.util.Optional;

public class ChecksumComparisonDTO {

    private Checksum checksum;
    private ChecksumIdentifier identifier;



    public ChecksumComparisonDTO() {
        this.checksum = new Checksum();
        this.identifier = ChecksumIdentifier.nullImplementation();
    }



    public boolean matches(ChecksumComparisonDTO other) {
        if (other == null) {
            return false;
        }

        return this.checksum.matches(other.checksum);
    }



    public void setChecksum(Checksum checksum) {
        this.checksum = Optional
            .ofNullable(checksum)
            .orElseGet(Checksum::new);
    }

    public void setIdentifier(ChecksumIdentifier identifier) {
        this.identifier = Optional
            .ofNullable(identifier)
            .orElseGet(ChecksumIdentifier::nullImplementation);
    }



    public String getHash() {
        return checksum.getHash();
    }

    public String getIdentification() {
        return identifier.getIdentification();
    }
}
