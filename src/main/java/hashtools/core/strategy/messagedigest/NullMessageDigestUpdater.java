package hashtools.core.strategy.messagedigest;

import java.security.MessageDigest;

public class NullMessageDigestUpdater implements MessageDigestUpdater {

    @Override
    public void update(MessageDigest messageDigest) {
        throw new IllegalStateException("The MessageDigestUpdater is not provided");
    }
}
