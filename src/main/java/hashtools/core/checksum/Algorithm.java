package hashtools.core.checksum;

import hashtools.core.strategy.messagedigest.MessageDigestUpdate;

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



    public Checksum generateChecksum(MessageDigestUpdate update) throws RuntimeException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(this.name);
            update.update(messageDigest);

            byte[] bytes = messageDigest.digest();
            StringBuilder hash = new StringBuilder();

            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }

            return new Checksum(hash.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Internal error regarding to the algorithm name. Report it to the developer.", e);
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
