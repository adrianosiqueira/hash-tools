package hashtools.core.domain;

import java.security.MessageDigest;

public class ChecksumGenerator {

    private MessageDigest messageDigest;



    public ChecksumGenerator(MessageDigest messageDigest) {
        this.messageDigest = messageDigest;
    }



    public void update(byte[] bytes) {
        messageDigest.update(bytes);
    }

    public void update(byte[] bytes, int length) {
        messageDigest.update(bytes, 0, length);
    }

    public Checksum decodeIntoChecksum() {
        var hash = new StringBuilder();

        for (var _byte : messageDigest.digest()) {
            hash.append(String.format("%02x", _byte));
        }

        return new Checksum(hash.toString());
    }
}
