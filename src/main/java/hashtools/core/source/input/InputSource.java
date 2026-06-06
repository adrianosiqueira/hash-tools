package hashtools.core.source.input;

import java.io.IOException;
import java.security.MessageDigest;

public interface InputSource {

    boolean isValid();

    void updateMessageDigest(MessageDigest messageDigest) throws IOException;

    String identify();
}
