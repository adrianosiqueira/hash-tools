package hashtools.core.model;

import hashtools.core.source.input.InputSource;

import java.io.IOException;
import java.util.Optional;

public class Checksum {

    private Algorithm algorithm;
    private String hash;



    public Checksum(String hash) {
        this.hash = Optional
            .ofNullable(hash)
            .orElse("");

        this.algorithm = Algorithm.getByLength(this.hash);
    }



    public static Checksum empty() {
        return new Checksum("");
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

    public Checksum generateChecksum(InputSource inputSource) throws IOException {
        return algorithm.generateChecksum(inputSource);
    }



    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public String getHash() {
        return hash;
    }
}
