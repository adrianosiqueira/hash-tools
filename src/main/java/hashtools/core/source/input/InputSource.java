package hashtools.core.source.input;

import hashtools.core.problem.Problem;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Optional;

public interface InputSource {

    Optional<Problem> checkForProblem();

    void updateMessageDigest(MessageDigest messageDigest) throws IOException;

    String identify();
}
