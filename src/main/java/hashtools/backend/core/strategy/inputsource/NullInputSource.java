package hashtools.backend.core.strategy.inputsource;

import hashtools.backend.core.interfaces.InputSource;

import java.security.MessageDigest;
import java.util.Optional;

public class NullInputSource implements InputSource {

    @Override
    public void updateMessageDigest(MessageDigest messageDigest) throws RuntimeException {
    }

    @Override
    public String getIdentification() {
        return "";
    }

    @Override
    public Optional<String> detectProblem() {
        return Optional.empty();
    }
}
