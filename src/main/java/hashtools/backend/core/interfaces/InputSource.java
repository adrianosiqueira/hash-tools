package hashtools.backend.core.interfaces;

import java.security.MessageDigest;
import java.util.Optional;

public interface InputSource {

    void updateMessageDigest(MessageDigest messageDigest) throws RuntimeException;

    String getIdentification();

    Optional<String> detectProblem();
}
