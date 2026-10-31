package hashtools.checker.domain;

import hashtools.core.domain.Checksum;

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

    public Checksum getOfficialChecksum() {
        return officialChecksum;
    }

    public void setOfficialChecksum(Checksum officialChecksum) {
        this.officialChecksum = officialChecksum;
    }

    public Checksum getGeneratedChecksum() {
        return generatedChecksum;
    }

    public void setGeneratedChecksum(Checksum generatedChecksum) {
        this.generatedChecksum = generatedChecksum;
    }
}
