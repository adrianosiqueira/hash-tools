package hashtools.core.strategy.inputsource;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Objects;

public class StringInputSource implements InputSource {

    private String string;



    public StringInputSource(String string) {
        this.string = Objects.requireNonNullElse(string, "");
    }



    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void updateMessageDigest(MessageDigest messageDigest) throws IOException {
        messageDigest.update(string.getBytes());
    }

    @Override
    public String identify() {
        return string;
    }
}
