package hashtools.domain.checksum;

import hashtools.domain.algorithm.Algorithm;

import java.util.Objects;

public class Checksum {

    private String hash;
    private Algorithm algorithm;



    private Checksum() {
    }



    public static Checksum createEmpty() {
        return Checksum.createFromHash("");
    }

    public static Checksum createFromHash(String hash) {
        Checksum checksum = new Checksum();
        checksum.hash = Objects.requireNonNull(hash);

        checksum.algorithm = Algorithm
            .getByLength(hash)
            .orElse(null);

        return checksum;
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
            ", valid=" + this.isValid() +
            '}';
    }
}
