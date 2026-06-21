package hashtools.core.strategy.messagedigest;

import java.security.MessageDigest;
import java.util.Objects;

public class TextMessageDigestUpdate implements MessageDigestUpdate {

    private String text;



    public TextMessageDigestUpdate(String text) {
        this.text = Objects.requireNonNull(text);
    }



    @Override
    public void update(MessageDigest messageDigest) throws RuntimeException {
        messageDigest.update(text.getBytes());
    }
}
