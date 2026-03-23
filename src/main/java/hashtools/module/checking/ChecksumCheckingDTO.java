package hashtools.module.checking;

import hashtools.core.model.Algorithm;
import hashtools.core.model.Checksum;
import hashtools.core.strategy.checksumidentifier.ChecksumIdentifier;
import hashtools.core.strategy.checksumidentifier.NullChecksumIdentifier;

public class ChecksumCheckingDTO {

    private Checksum officialChecksum;
    private Checksum generatedChecksum;
    private ChecksumIdentifier identifier;



    public ChecksumCheckingDTO() {
        this.officialChecksum = new Checksum();
        this.generatedChecksum = new Checksum();
        this.identifier = new NullChecksumIdentifier();
    }



    public boolean matches() {
        return officialChecksum != null
            && officialChecksum.matches(generatedChecksum);
    }



    public Algorithm getAlgorithm() {
        return officialChecksum.getAlgorithm();
    }

    public void setOfficialChecksum(Checksum officialChecksum) {
        this.officialChecksum = officialChecksum;
    }

    public String getOfficialHash() {
        return officialChecksum.getHash();
    }

    public void setGeneratedChecksum(Checksum generatedChecksum) {
        this.generatedChecksum = generatedChecksum;
    }

    public String getGeneratedHash() {
        return generatedChecksum.getHash();
    }

    public void setIdentifier(ChecksumIdentifier identifier) {
        this.identifier = identifier;
    }

    public String getIdentification() {
        return identifier.getIdentification();
    }
}
