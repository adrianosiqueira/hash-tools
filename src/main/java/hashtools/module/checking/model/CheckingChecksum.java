package hashtools.module.checking.model;

import hashtools.core.model.Algorithm;
import hashtools.core.model.Checksum;
import hashtools.core.strategy.checksumidentifier.ChecksumIdentifier;

import java.util.Optional;

public class CheckingChecksum {

    private Checksum officialChecksum;
    private Checksum generatedChecksum;
    private ChecksumIdentifier identifier;



    public CheckingChecksum() {
        this.officialChecksum = Checksum.empty();
        this.generatedChecksum = Checksum.empty();
        this.identifier = ChecksumIdentifier.nullImplementation();
    }



    public boolean matches() {
        return officialChecksum != null
            && officialChecksum.matches(generatedChecksum);
    }



    public void setOfficialChecksum(Checksum officialChecksum) {
        this.officialChecksum = Optional
            .ofNullable(officialChecksum)
            .orElseGet(Checksum::empty);
    }

    public void setGeneratedChecksum(Checksum generatedChecksum) {
        this.generatedChecksum = Optional
            .ofNullable(generatedChecksum)
            .orElseGet(Checksum::empty);
    }

    public void setIdentifier(ChecksumIdentifier identifier) {
        this.identifier = identifier;
    }



    public Algorithm getAlgorithm() {
        return officialChecksum.getAlgorithm();
    }

    public String getOfficialHash() {
        return officialChecksum.getHash();
    }

    public String getGeneratedHash() {
        return generatedChecksum.getHash();
    }

    public String getIdentification() {
        return identifier.getIdentification();
    }
}
