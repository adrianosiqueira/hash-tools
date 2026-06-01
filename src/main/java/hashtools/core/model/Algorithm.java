package hashtools.core.model;

import hashtools.core.strategy.inputsource.InputSource;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public enum Algorithm {

    NULL_ALGORITHM(0, "", ""),
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



    public static Algorithm getByLength(String hash) {
        int searchLength = Objects
            .requireNonNullElse(hash, "")
            .length();

        for (Algorithm algorithm : Algorithm.values()) {
            if (algorithm.length == searchLength) {
                return algorithm;
            }
        }

        return NULL_ALGORITHM;
    }

    public static Algorithm getByName(String name) {
        String searchName = Objects
            .requireNonNullElse(name, "")
            .toLowerCase()
            .replaceAll("[^a-z0-9]", "");

        for (Algorithm algorithm : Algorithm.values()) {
            if (algorithm.name.equals(searchName)) {
                return algorithm;
            }
        }

        return NULL_ALGORITHM;
    }



    public int getLength() {
        return length;
    }

    public String getDisplayName() {
        return displayName;
    }



    public Checksum generateChecksum(InputSource inputSource) throws IOException {
        if (this == NULL_ALGORITHM) {
            return new Checksum("");
        }



        try {
            MessageDigest messageDigest = MessageDigest.getInstance(this.name);
            inputSource.updateMessageDigest(messageDigest);

            byte[] digest = messageDigest.digest();
            StringBuilder hash = new StringBuilder();

            for (byte b : digest) {
                hash.append(String.format("%02x", b));
            }

            return new Checksum(hash.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Internal error with the algorithm name.", e);
        }
    }
}
