package hashtools.strategy.inputsource;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Optional;

public interface InputSource {

    default void updateMessageDigest(Collection<MessageDigest> messageDigests) throws IOException {
    }

    default String getIdentification() {
        return "";
    }

    default Optional<String> detectProblem() {
        return Optional.empty();
    }

    default void cancel() {
    }
}
