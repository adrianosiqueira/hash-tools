package hashtools.core.engine;

import hashtools.core.model.Algorithm;
import hashtools.core.model.Checksum;
import hashtools.core.strategy.messagedigest.MessageDigestUpdater;

import java.security.MessageDigest;

public class ChecksumGenerator {

    public Checksum generate(Algorithm algorithm, MessageDigestUpdater updater) {
        try {
            MessageDigest messageDigest = algorithm.createMessageDigest();
            updater.update(messageDigest);

            byte[] bytes = messageDigest.digest();
            String hash = this.decodeBytesToHexadecimal(bytes);

            return new Checksum(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    private String decodeBytesToHexadecimal(byte[] bytes) {
        StringBuilder result = new StringBuilder();

        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }

        return result.toString();
    }
}
