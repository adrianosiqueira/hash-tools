package hashtools.backend.core.strategy.inputsource;

import hashtools.backend.core.interfaces.InputSource;

import java.security.MessageDigest;
import java.util.Optional;

public class TextInputSource implements InputSource {

    private String text;



    public TextInputSource(String text) {
        this.text = text;
    }



    @Override
    public void updateMessageDigest(MessageDigest messageDigest) throws RuntimeException {
        messageDigest.update(text.getBytes());
    }

    @Override
    public String getIdentification() {
        return text;
    }

    @Override
    public Optional<String> detectProblem() {
        return Optional.empty();
    }
}
