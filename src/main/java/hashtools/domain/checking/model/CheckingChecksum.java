package hashtools.domain.checking.model;

import hashtools.core.checksum.Checksum;

import java.util.Objects;

public class CheckingChecksum {

    private Checksum officialChecksum;
    private Checksum generatedChecksum;



    public CheckingChecksum() {
        this.officialChecksum = new Checksum();
        this.generatedChecksum = new Checksum();
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
        this.officialChecksum = Objects.requireNonNullElse(officialChecksum, new Checksum());
    }

    public void setGeneratedChecksum(Checksum generatedChecksum) {
        this.generatedChecksum = Objects.requireNonNullElse(generatedChecksum, new Checksum());
    }
}
