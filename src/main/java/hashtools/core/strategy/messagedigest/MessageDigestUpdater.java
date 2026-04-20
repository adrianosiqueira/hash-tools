package hashtools.core.strategy.messagedigest;

import java.security.MessageDigest;

public interface MessageDigestUpdater {

    static MessageDigestUpdater nullImplementation() {
        return _ -> {};
    }



    void update(MessageDigest messageDigest) throws Exception;
}
