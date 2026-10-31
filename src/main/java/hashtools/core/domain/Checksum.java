package hashtools.core.domain;

import java.util.Objects;

public class Checksum {

    private String hash;
    private Algorithm algorithm;



    public Checksum() {
        this.hash = "";
        this.algorithm = null;
    }

    public Checksum(String hash) {
        this.hash = Objects.requireNonNull(hash);
        this.algorithm = Algorithm.getByLength(hash).orElse(null);
    }



    public boolean matches(Checksum checksum) {
        return checksum != null
            && this.hash.equalsIgnoreCase(checksum.hash)
            && this.algorithm == checksum.algorithm;
    }

    public boolean isValid() {
        return algorithm != null;
    }

    public String getHash() {
        return hash;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }
}
