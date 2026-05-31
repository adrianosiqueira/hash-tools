package hashtools.module.checking.model;

import hashtools.core.model.Checksum;

import java.util.Optional;

public class CheckingChecksum {

    private Checksum officialChecksum;
    private Checksum generatedChecksum;



    public CheckingChecksum() {
        this.officialChecksum = Checksum.empty();
        this.generatedChecksum = Checksum.empty();
    }



    public boolean matches() {
        return officialChecksum != null
            && officialChecksum.matches(generatedChecksum);
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
