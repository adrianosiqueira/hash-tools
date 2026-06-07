package hashtools.core.source.input;

import hashtools.core.problem.Problem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.Optional;

public class FileInputSource implements InputSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileInputSource.class);



    private String filePath;



    public FileInputSource(String filePath) {
        this.filePath = Objects.requireNonNullElse(filePath, "");
    }



    @Override
    public Optional<Problem> checkForProblem() {
        LOGGER.info("Validating the input source.");
        Path path = Path.of(filePath);

        if (Files.notExists(path)) {
            Problem problem = new Problem()
                .withDescription("The input file does not exist.")
                .withCause("The file may be deleted after selection or you entered a incorrect file path.")
                .withFix("Use the 'open' button to properly select the input file.");

            LOGGER.warn("Found: {}", problem);
            return Optional.of(problem);
        }

        if (!Files.isRegularFile(path)) {
            Problem problem = new Problem()
                .withDescription("The input is not a file.")
                .withCause("You may incorrectly entered the file path.")
                .withFix("Use the 'open' button to properly select a file.");

            LOGGER.warn("Found: {}", problem);
            return Optional.of(problem);
        }

        LOGGER.info("No problem found.");
        return Optional.empty();
    }

    @Override
    public void updateMessageDigest(MessageDigest messageDigest) throws IOException {
        Path path = Path.of(filePath);

        try (InputStream stream = Files.newInputStream(path)) {
            int oneMebibyte = 1024 * 1024;
            int eof = -1;
            int bufferOffset = 0;

            byte[] buffer = new byte[oneMebibyte];
            int read;

            while ((read = stream.read(buffer)) != eof) {
                messageDigest.update(buffer, bufferOffset, read);
            }
        }
    }

    @Override
    public String identify() {
        return Path
            .of(filePath)
            .toAbsolutePath()
            .toString();
    }
}
