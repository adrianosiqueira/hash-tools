package hash_tools.backend.checksum.checksum_source;

import java.security.MessageDigest;

public class StringChecksumSource extends ChecksumSource {

    private final String string;



    public StringChecksumSource(String string) {
        this.string = string;
    }



    @Override
    public String identify() {
        return string;
    }

    @Override
    protected void updateMessageDigest(MessageDigest messageDigest) throws RuntimeException {
        messageDigest.update(string.getBytes());
    }
}
