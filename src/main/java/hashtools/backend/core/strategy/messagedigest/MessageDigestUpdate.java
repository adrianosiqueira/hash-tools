package hashtools.backend.core.strategy.messagedigest;

import java.security.MessageDigest;

public interface MessageDigestUpdate {

    void update(MessageDigest messageDigest) throws RuntimeException;
}
