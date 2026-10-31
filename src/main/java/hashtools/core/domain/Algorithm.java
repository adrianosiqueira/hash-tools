package hashtools.core.domain;

import java.util.Optional;

public enum Algorithm {

    MD5(32, "md5"),
    SHA1(40, "sha1"),
    SHA224(56, "sha224"),
    SHA256(64, "sha256"),
    SHA384(96, "sha384"),
    SHA512(128, "sha512");



    private final int length;
    private final String name;



    Algorithm(int length, String name) {
        this.length = length;
        this.name = name;
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
}
