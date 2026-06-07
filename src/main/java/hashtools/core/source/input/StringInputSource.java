package hashtools.core.source.input;

import hashtools.core.problem.Problem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.Optional;

public class StringInputSource implements InputSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringInputSource.class);



    private String string;



    public StringInputSource(String string) {
        this.string = Objects.requireNonNullElse(string, "");
    }



    @Override
    public Optional<Problem> checkForProblem() {
        LOGGER.info("Validating the input source.");
        LOGGER.info("No problem found.");
        return Optional.empty();
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
