package hashtools.domain.checksum;

public class ChecksumPair {

    private Checksum checksum1;
    private Checksum checksum2;



    public ChecksumPair() {
        this.checksum1 = Checksum.createEmpty();
        this.checksum2 = Checksum.createEmpty();
    }



    public boolean matches() {
        return checksum1 != null
            && checksum1.matches(checksum2);
    }

    public void setOfficialChecksum(Checksum checksum) {
        this.checksum1 = checksum;
    }

    public Checksum getOfficialChecksum() {
        return checksum1;
    }

    public void setGeneratedChecksum(Checksum checksum) {
        this.checksum2 = checksum;
    }

    public Checksum getGeneratedChecksum() {
        return checksum2;
    }

    public void setGeneratedChecksum1(Checksum checksum) {
        this.checksum1 = checksum;
    }

    public Checksum getGeneratedChecksum1() {
        return checksum1;
    }

    public void setGeneratedChecksum2(Checksum checksum) {
        this.checksum2 = checksum;
    }

    public Checksum getGeneratedChecksum2() {
        return checksum2;
    }
}
