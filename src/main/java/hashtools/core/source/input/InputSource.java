package hashtools.core.source.input;

import hashtools.core.problem.Problem;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.function.Consumer;

public interface InputSource {

    boolean checkForProblem(Consumer<Problem> problemConsumer);

    void updateMessageDigest(MessageDigest messageDigest) throws IOException;

    String identify();
}
