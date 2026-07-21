package hashtools.domain.checksum;

import hashtools.domain.algorithm.Algorithm;
import hashtools.domain.algorithm.ChecksumGenerator;

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



    public static Checksum createEmpty() {
        return new Checksum("");
    }

    public static Checksum createFromHash(String hash) {
        return new Checksum(hash);
    }



    public boolean isValid() {
        return algorithm != null;
    }

    public boolean matches(Checksum checksum) {
        return checksum != null
            && this.hash.equalsIgnoreCase(checksum.hash)
            && this.algorithm == checksum.algorithm;
    }

    public ChecksumGenerator createMessageDigestProxy() {
        return algorithm.createMessageDigestProxy();
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
