package hashtools.domain.algorithm;

import hashtools.domain.checksum.Checksum;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record MessageDigestProxy(
    MessageDigest messageDigest
) {

    public static MessageDigestProxy fromAlgorithm(Algorithm algorithm) {
        try {
            String name = algorithm.getName();
            MessageDigest messageDigest = MessageDigest.getInstance(name);

            return new MessageDigestProxy(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static MessageDigestProxy fromChecksum(Checksum checksum) {
        Algorithm algorithm = checksum.getAlgorithm();

        return MessageDigestProxy.fromAlgorithm(algorithm);
    }



    public Checksum decodeIntoChecksum() {
        byte[] bytes = messageDigest.digest();

        String hash = IntStream
            .range(0, bytes.length)
            .mapToObj(i -> bytes[i])
            .map("%02x"::formatted)
            .collect(Collectors.joining());

        return new Checksum(hash);
    }
}
