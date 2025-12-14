package hash_tools.domain.checksum_source;

import hash_tools.domain.checksum.Algorithm;
import hash_tools.domain.checksum.Checksum;

import java.security.MessageDigest;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class ChecksumSource {

    public Checksum generateChecksum(Algorithm algorithm) throws RuntimeException {
        MessageDigest messageDigest = createMessageDigest(algorithm);
        updateMessageDigest(messageDigest);

        byte[] bytes = messageDigest.digest();
        String decoded = decode(bytes);

        return Checksum.fromValue(decoded);
    }



    public abstract String identify();

    protected abstract void updateMessageDigest(MessageDigest messageDigest) throws RuntimeException;



    protected MessageDigest createMessageDigest(Algorithm algorithm) {
        try {
            return MessageDigest.getInstance(algorithm.algorithm());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String decode(byte[] bytes) {
        return IntStream
            .range(0, bytes.length)
            .mapToObj(i -> bytes[i])
            .map("%02x"::formatted)
            .collect(Collectors.joining());
    }
}
