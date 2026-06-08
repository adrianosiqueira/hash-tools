package hashtools.core.source.checksum;

import hashtools.core.checksum.Checksum;
import hashtools.core.file.FileExtension;
import hashtools.core.problem.Problem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class FileChecksumSource implements ChecksumSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileChecksumSource.class);



    private String filePath;



    public FileChecksumSource(String filePath) {
        this.filePath = Objects.requireNonNullElse(filePath, "");
    }



    @Override
    public Optional<Problem> checkForProblem() {
        LOGGER.info("Validating the checksum source.");
        Path path = Path.of(filePath);

        if (Files.notExists(path)) {
            Problem problem = new Problem()
                .withDescription("The checksum file does not exist.")
                .withCause("The file may be deleted after selection or you entered a incorrect file path.")
                .withFix("Use the 'open' button to properly select the checksum file.");

            LOGGER.warn("Found: {}", problem);
            return Optional.of(problem);
        }

        if (!Files.isRegularFile(path)) {
            Problem problem = new Problem()
                .withDescription("The checksum file is not a file.")
                .withCause("You may incorrectly entered the file path or the path ends in a directory.")
                .withFix("Use the 'open' button to properly select the checksum file.");

            LOGGER.warn("Found: {}", problem);
            return Optional.of(problem);
        }

        if (this.checksumFileHasInvalidExtension()) {
            Problem problem = new Problem()
                .withDescription("The checksum file is not valid.")
                .withCause("You are attempting to use a file with an invalid extension.")
                .withFix("Use the 'open' button to properly select the checksum file.");

            LOGGER.warn("Found: {}", problem);
            return Optional.of(problem);
        }

        LOGGER.info("No problem found.");
        return Optional.empty();
    }

    @Override
    public List<Checksum> getValidChecksums() throws IOException {
        List<Checksum> checksums = new ArrayList<>();



        Path path = Path.of(filePath);

        try (Stream<String> lines = Files.lines(path)) {
            lines
                .map(line -> line.split(" ")[0])
                .map(Checksum::new)
                .filter(Checksum::isValid)
                .forEach(checksums::add);
        }



        return checksums;
    }



    private boolean checksumFileHasInvalidExtension() {
        int lastDotIndex = filePath.lastIndexOf('.');

        String extension = lastDotIndex > 0
            ? filePath.substring(lastDotIndex + 1)
            : "";



        return !FileExtension
            .HASH
            .containsExtension(extension);
    }
}
