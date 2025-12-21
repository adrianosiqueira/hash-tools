package hash_tools.backend.checksum;

public class ComparingChecksum {

    private Checksum generatedChecksum1;
    private Checksum generatedChecksum2;
    private boolean valid;



    public ComparingChecksum(Checksum generatedChecksum1, Checksum generatedChecksum2) {
        this.generatedChecksum1 = generatedChecksum1;
        this.generatedChecksum2 = generatedChecksum2;

        this.valid = generatedChecksum1 != null
            && generatedChecksum1.valid()
            && generatedChecksum2 != null
            && generatedChecksum2.valid();
    }



    public boolean matches() {
        return generatedChecksum1 != null
            && generatedChecksum1.matches(generatedChecksum2);
    }



    public Checksum generatedChecksum1() {
        return generatedChecksum1;
    }

    public Checksum generatedChecksum2() {
        return generatedChecksum2;
    }

    public boolean valid() {
        return valid;
    }
}
