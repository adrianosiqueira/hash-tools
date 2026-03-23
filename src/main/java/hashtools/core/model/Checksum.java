package hashtools.core.model;

import java.util.Optional;

public class Checksum {

    private Algorithm algorithm;
    private String hash;



    public Checksum() {
        this("");
    }

    public Checksum(String hash) {
        this.hash = Optional
            .ofNullable(hash)
            .orElse("");

        this.algorithm = Algorithm
            .getByLength(this.hash.length())
            .orElse(null);
    }



    public boolean matches(Checksum other) {
        if (other == null) {
            return false;
        } else if (this.algorithm != other.algorithm) {
            return false;
        }

        return this.hash.equalsIgnoreCase(other.hash);
    }

    public boolean isValid() {
        return algorithm != null;
    }



    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public String getHash() {
        return hash;
    }
}
