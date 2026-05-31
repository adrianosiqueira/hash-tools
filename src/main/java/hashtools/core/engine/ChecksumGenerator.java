package hashtools.core.engine;

import hashtools.core.model.Algorithm;
import hashtools.core.model.Checksum;
import hashtools.core.strategy.messagedigest.MessageDigestUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

@Deprecated(forRemoval = true)
public class ChecksumGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChecksumGenerator.class);



    public Checksum generate(Algorithm algorithm, MessageDigestUpdater updater) throws Exception {
        MessageDigest messageDigest = algorithm.createMessageDigest();
        updater.update(messageDigest);

        byte[] bytes = messageDigest.digest();
        String hash = this.decodeBytesToHexadecimal(bytes);

        LOGGER.info("Generated '{}' checksum for '{}'.", algorithm.getDisplayName(), updater);
        return new Checksum(hash);
    }



    private String decodeBytesToHexadecimal(byte[] bytes) {
        StringBuilder result = new StringBuilder();

        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }

        return result.toString();
    }
}
