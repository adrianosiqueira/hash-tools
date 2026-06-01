package hashtools.core.strategy.inputsource;

import java.io.IOException;
import java.security.MessageDigest;

public interface InputSource {

    boolean isValid();

    void updateMessageDigest(MessageDigest messageDigest) throws IOException;

    String identify();
}
