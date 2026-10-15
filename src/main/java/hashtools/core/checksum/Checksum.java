package hashtools.core.checksum;

public class Checksum {

    private String hash;
    private Algorithm algorithm;



    public Checksum() {
        this.hash = "";
        this.algorithm = null;
    }

    public Checksum(String hash) {
        this.hash = hash;
        this.algorithm = Algorithm.getByLength(hash).orElse(null);
    }



    public boolean isValid() {
        return algorithm != null;
    }

    public boolean matches(Checksum checksum) {
        return checksum != null
            && this.hash.equals(checksum.hash)
            && this.algorithm == checksum.algorithm;
    }

    public String getHash() {
        return hash;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }
}
