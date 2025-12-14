package hash_tools.domain.checksum;

public class CheckingChecksum {

    private Checksum officialChecksum;
    private Checksum generatedChecksum;
    private boolean valid;



    public CheckingChecksum(Checksum officialChecksum, Checksum generatedChecksum) {
        this.officialChecksum = officialChecksum;
        this.generatedChecksum = generatedChecksum;

        this.valid = officialChecksum != null
            && officialChecksum.valid()
            && generatedChecksum != null
            && generatedChecksum.valid();
    }



    public boolean matches() {
        return officialChecksum != null
            && officialChecksum.matches(generatedChecksum);
    }



    public Checksum officialChecksum() {
        return officialChecksum;
    }

    public Checksum generatedChecksum() {
        return generatedChecksum;
    }

    public boolean valid() {
        return valid;
    }
}
