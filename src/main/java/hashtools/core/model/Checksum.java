package hashtools.core.model;

import java.util.Optional;

public class Checksum {

    private Algorithm algorithm;
    private String hash;



    public Checksum(String hash) {
        this.hash = Optional
            .ofNullable(hash)
            .orElse("");

        this.algorithm = Algorithm
            .getByLength(this.hash.length())
            .orElse(null);
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
