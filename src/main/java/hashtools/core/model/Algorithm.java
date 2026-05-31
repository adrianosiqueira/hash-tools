package hashtools.core.model;

import hashtools.core.source.input.InputSource;

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

    @Deprecated(forRemoval = true)
    public static Algorithm getDefault() {
        return Algorithm.MD5;
    }



    public int getLength() {
        return length;
    }

    public String getDisplayName() {
        return displayName;
    }



    @Deprecated(forRemoval = true)
    public MessageDigest createMessageDigest() {
        try {
            return MessageDigest.getInstance(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public String generateChecksum(InputSource inputSource) throws IOException {
        if (this == NULL_ALGORITHM) {
            return "";
        }



        try {
            MessageDigest messageDigest = MessageDigest.getInstance(this.name);
            inputSource.updateMessageDigest(messageDigest);

            byte[] digest = messageDigest.digest();
            StringBuilder hash = new StringBuilder();

            for (byte b : digest) {
                hash.append(String.format("%02x", b));
            }

            return hash.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Internal error with the algorithm name.", e);
        }
    }
}
