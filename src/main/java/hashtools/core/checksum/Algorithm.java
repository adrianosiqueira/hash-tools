package hashtools.core.checksum;

import java.util.Optional;

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
        var searchLength = hash.length();

        for (var algorithm : Algorithm.values()) {
            if (algorithm.length == searchLength) {
                return Optional.of(algorithm);
            }
        }

        return Optional.empty();
    }

    public static Optional<Algorithm> getByName(String name) {
        var searchName = name.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

        for (var algorithm : Algorithm.values()) {
            if (algorithm.name.equals(searchName)) {
                return Optional.of(algorithm);
            }
        }

        return Optional.empty();
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
