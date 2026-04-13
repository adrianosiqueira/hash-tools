package hashtools.core.strategy.messagedigest;

import java.security.MessageDigest;
import java.util.Objects;
import java.util.Optional;

public class StringMessageDigestUpdater implements MessageDigestUpdater {

    private final String string;



    public StringMessageDigestUpdater(String string) {
        this.string = Optional
            .ofNullable(string)
            .orElse("");
    }



    @Override
    public void update(MessageDigest messageDigest) {
        Objects.requireNonNull(messageDigest, "The message digest cannot be null");

        messageDigest.update(string.getBytes());
    }
}
