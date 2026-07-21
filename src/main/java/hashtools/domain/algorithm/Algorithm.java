package hashtools.domain.algorithm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
    private final String name;
    private final String displayName;



    Algorithm(int length, String name, String displayName) {
        this.length = length;
        this.name = name;
        this.displayName = displayName;
    }



    public static Optional<Algorithm> getByLength(String hash) {
        int searchLength = Objects
            .requireNonNullElse(hash, "")
            .length();

        return Stream
            .of(Algorithm.values())
            .filter(algorithm -> algorithm.length == searchLength)
            .findFirst();
    }

    public static Optional<Algorithm> getByName(String name) {
        String searchName = Objects
            .requireNonNullElse(name, "")
            .toLowerCase()
            .replaceAll("[^a-z0-9]", "");

        return Stream
            .of(Algorithm.values())
            .filter(algorithm -> algorithm.name.equals(searchName))
            .findFirst();
    }

    public static List<Algorithm> getAllAscendingSortedByLength() {
        return Stream
            .of(Algorithm.values())
            .sorted(Comparator.comparing(Algorithm::getLength))
            .toList();
    }



    public ChecksumGenerator createMessageDigestProxy() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(this.getName());
            return new ChecksumGenerator(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }



    public int getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }
}
