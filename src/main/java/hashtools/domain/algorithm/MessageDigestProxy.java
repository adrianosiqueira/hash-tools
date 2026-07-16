package hashtools.domain.algorithm;

import hashtools.domain.checksum.Checksum;

import java.security.MessageDigest;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record MessageDigestProxy(
    MessageDigest messageDigest
) {

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
