package hashtools.module.checking;

import hashtools.core.model.Algorithm;
import hashtools.core.model.Checksum;
import hashtools.core.strategy.checksumidentifier.ChecksumIdentifier;

import java.util.Optional;

public class ChecksumCheckingDTO {

    private Checksum officialChecksum;
    private Checksum generatedChecksum;
    private ChecksumIdentifier identifier;



    public ChecksumCheckingDTO() {
        this.officialChecksum = new Checksum();
        this.generatedChecksum = new Checksum();
        this.identifier = ChecksumIdentifier.nullImplementation();
    }



    public boolean matches() {
        return officialChecksum != null
            && officialChecksum.matches(generatedChecksum);
    }



    public void setOfficialChecksum(Checksum officialChecksum) {
        this.officialChecksum = Optional
            .ofNullable(officialChecksum)
            .orElseGet(Checksum::new);
    }

    public void setGeneratedChecksum(Checksum generatedChecksum) {
        this.generatedChecksum = Optional
            .ofNullable(generatedChecksum)
            .orElseGet(Checksum::new);
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
