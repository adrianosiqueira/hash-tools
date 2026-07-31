package hashtools.domain.algorithm;

import hashtools.domain.checksum.Checksum;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record ChecksumGenerator(
    MessageDigest messageDigest
) {

    private static final int BUFFER_OFFSET = 0;



    public static ChecksumGenerator createFromAlgorithm(Algorithm algorithm) {
        try {
            String name = algorithm.name();
            MessageDigest messageDigest = MessageDigest.getInstance(name);

            return new ChecksumGenerator(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static ChecksumGenerator createFromChecksum(Checksum checksum) {
        Algorithm algorithm = checksum.getAlgorithm();

        return ChecksumGenerator.createFromAlgorithm(algorithm);
    }



    public void receiveBytes(byte[] buffer) {
        this.receiveBytes(buffer, buffer.length);
    }

    public void receiveBytes(byte[] buffer, int length) {
        messageDigest.update(buffer, BUFFER_OFFSET, length);
    }

    public Checksum decodeIntoChecksum() {
        byte[] bytes = messageDigest.digest();

        String hash = IntStream
            .range(0, bytes.length)
            .mapToObj(i -> bytes[i])
            .map("%02x"::formatted)
            .collect(Collectors.joining());

        return Checksum.createFromHash(hash);
    }
}
