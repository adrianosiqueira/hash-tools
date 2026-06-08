package hashtools.domain.checksum.checker.model;

import hashtools.core.checksum.Checksum;

import java.util.Optional;

public class CheckerChecksum {

    private Checksum officialChecksum;
    private Checksum generatedChecksum;



    public CheckerChecksum() {
        this.officialChecksum = new Checksum();
        this.generatedChecksum = new Checksum();
    }



    public boolean matches() {
        return officialChecksum.matches(generatedChecksum);
    }

    public String getAlgorithmDisplayName() {
        return officialChecksum.getAlgorithmDisplayName();
    }

    public String getOfficialHash() {
        return officialChecksum.getHash();
    }

    public String getGeneratedHash() {
        return generatedChecksum.getHash();
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
}
