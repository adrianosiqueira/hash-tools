package hashtools.core.strategy.messagedigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Objects;
import java.util.Optional;

public class StringMessageDigestUpdater implements MessageDigestUpdater {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringMessageDigestUpdater.class);



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
        LOGGER.info("MessageDigest updated from '{}'.", string);
    }
}
