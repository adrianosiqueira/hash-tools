package hashtools.backend.core.checksum;

import java.util.Objects;

public class Checksum {

    private String hash;
    private Algorithm algorithm;



    public Checksum() {
        this("");
    }

    public Checksum(String hash) {
        this.hash = Objects.requireNonNull(hash);

        this.algorithm = Algorithm
            .getByLength(hash)
            .orElse(null);
    }



    public boolean isValid() {
        return algorithm != null;
    }

    public boolean matches(Checksum checksum) {
        return checksum != null
            && this.hash.equalsIgnoreCase(checksum.hash)
            && this.algorithm == checksum.algorithm;
    }



    public String getHash() {
        return hash;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    @Override
    public String toString() {
        return "Checksum{" +
            "hash='" + hash + '\'' +
            ", algorithm=" + algorithm +
            '}';
    }
}
