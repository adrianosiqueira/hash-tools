package hashtools.domain.checker.model;

import hashtools.core.problem.Problem;
import hashtools.view.dialog.FileExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

public class CheckerScreenInput {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckerScreenInput.class);



    private String input;
    private boolean usingInputFile;

    private String checksum;
    private boolean usingChecksumFile;



    public CheckerScreenInput() {
        this.input = "";
        this.usingInputFile = true;

        this.checksum = "";
        this.usingChecksumFile = true;
    }



    public Optional<Problem> identifyProblem() {
        LOGGER.info("Validating the input data.");

        if (usingInputFile) {
            Path inputFile = Path.of(input);

            if (!Files.isRegularFile(inputFile)) {
                Problem problem = new Problem()
                    .withDescription("The input is not a file.")
                    .withCause("You may incorrectly entered the file path.")
                    .withFix("Use the 'open' button to properly select a file.");

                LOGGER.warn("Found: {}", problem);
                return Optional.of(problem);
            }
        }



        if (usingChecksumFile) {
            Path checksumFile = Path.of(checksum);

            if (Files.notExists(checksumFile)) {
                Problem problem = new Problem()
                    .withDescription("The checksum file does not exist.")
                    .withCause("The file may be deleted after selection or you entered a incorrect file path.")
                    .withFix("Use the 'open' button to properly select the checksum file.");

                LOGGER.warn("Found: {}", problem);
                return Optional.of(problem);
            } else if (!Files.isRegularFile(checksumFile)) {
                Problem problem = new Problem()
                    .withDescription("The checksum file is not a file.")
                    .withCause("You may incorrectly entered the file path or the path ends in a directory.")
                    .withFix("Use the 'open' button to properly select the checksum file.");

                LOGGER.warn("Found: {}", problem);
                return Optional.of(problem);
            } else if (this.checksumFileHasInvalidExtension()) {
                Problem problem = new Problem()
                    .withDescription("The checksum file is not valid.")
                    .withCause("You are attempting to use a file with an invalid extension.")
                    .withFix("Use the 'open' button to properly select the checksum file.");

                LOGGER.warn("Found: {}", problem);
                return Optional.of(problem);
            }
        }



        LOGGER.info("No problem found.");
        return Optional.empty();
    }

    public void setInput(String input) {
        this.input = Objects.requireNonNullElse(input, "");
    }

    public void setUsingInputFile(boolean usingInputFile) {
        this.usingInputFile = usingInputFile;
    }

    public void setChecksum(String checksum) {
        this.checksum = Objects.requireNonNullElse(checksum, "");
    }

    public void setUsingChecksumFile(boolean usingChecksumFile) {
        this.usingChecksumFile = usingChecksumFile;
    }



    private boolean checksumFileHasInvalidExtension() {
        int lastDotIndex = checksum.lastIndexOf('.');

        String extension = lastDotIndex > 0
            ? checksum.substring(lastDotIndex + 1)
            : "";



        return !FileExtension
            .HASH
            .containsExtension(extension);
    }
}
