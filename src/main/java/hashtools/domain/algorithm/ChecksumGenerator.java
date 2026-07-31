package hashtools.domain.algorithm;

import hashtools.domain.checksum.Checksum;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ChecksumGenerator {

    private static final int BUFFER_OFFSET = 0;



    private MessageDigest messageDigest;



    private ChecksumGenerator() {
    }



    public static ChecksumGenerator createFromAlgorithm(Algorithm algorithm) {
        try {
            String name = algorithm.name();

            ChecksumGenerator generator = new ChecksumGenerator();
            generator.messageDigest = MessageDigest.getInstance(name);

            return generator;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static ChecksumGenerator createFromChecksum(Checksum checksum) {
        Algorithm algorithm = checksum.getAlgorithm();

        return ChecksumGenerator.createFromAlgorithm(algorithm);
    }



    public void receiveBytes(byte[] buffer) {
        this.receiveBytes(
            buffer,
            buffer.length
        );
    }

    public void receiveBytes(byte[] buffer, int length) {
        messageDigest.update(
            buffer,
            BUFFER_OFFSET,
            length
        );
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
