package hashtools.core.model;

import hashtools.core.source.input.InputSource;

import java.io.IOException;
import java.util.Objects;

public class Checksum {

    private Algorithm algorithm;
    private String hash;



    public Checksum() {
        this("");
    }

    public Checksum(String hash) {
        this.hash = Objects.requireNonNullElse(hash, "");
        this.algorithm = Algorithm.getByLength(this.hash);
    }



    @Deprecated(forRemoval = true)
    public static Checksum empty() {
        return new Checksum("");
    }



    public boolean matches(Checksum other) {
        return other != null
            && this.algorithm == other.algorithm
            && this.hash.equalsIgnoreCase(other.hash);
    }

    public boolean isValid() {
        return algorithm != null;
    }

    public String getAlgorithmDisplayName() {
        return algorithm.getDisplayName();
    }

    public String getHash() {
        return hash;
    }

    public Checksum generateChecksum(InputSource inputSource) throws IOException {
        return algorithm.generateChecksum(inputSource);
    }
}
