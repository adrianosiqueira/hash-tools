package hash_tools.domain.checksum;

import java.util.Optional;
import java.util.stream.Stream;

public enum Algorithm {

    MD5(32, "md5", "MD5"),
    SHA1(40, "sha1", "SHA-1"),
    SHA224(56, "sha224", "SHA-224"),
    SHA256(64, "sha256", "SHA-256"),
    SHA384(96, "sha384", "SHA-384"),
    SHA512(128, "sha512", "SHA-512");



    private final int length;
    private final String algorithm;
    private final String displayName;



    Algorithm(int length, String algorithm, String displayName) {
        this.length = length;
        this.algorithm = algorithm;
        this.displayName = displayName;
    }



    public static Optional<Algorithm> fromLength(int length) {
        return Stream
            .of(values())
            .filter(algorithm -> algorithm.length == length)
            .findFirst();
    }

    public static Optional<Algorithm> fromName(String name) {
        String searchName = name
            .toLowerCase()
            .replaceAll("[^a-z0-9]", "");

        return Stream
            .of(values())
            .filter(algorithm -> algorithm.algorithm.equals(searchName))
            .findFirst();
    }



    public int length() {
        return length;
    }

    public String algorithm() {
        return algorithm;
    }

    public String displayName() {
        return displayName;
    }
}
