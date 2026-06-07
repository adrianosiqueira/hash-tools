package hashtools.core.source.checksum;

import hashtools.core.checksum.Checksum;
import hashtools.core.problem.Problem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class StringChecksumSource implements ChecksumSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringChecksumSource.class);



    private String string;



    public StringChecksumSource(String string) {
        this.string = Objects.requireNonNullElse(string, "");
    }



    @Override
    public Optional<Problem> checkForProblem() {
        LOGGER.info("Validating the checksum source.");
        LOGGER.info("No problem found.");
        return Optional.empty();
    }

    @Override
    public List<Checksum> getValidChecksums() throws IOException {
        List<Checksum> checksums = new ArrayList<>();



        Stream
            .of(string.split("\n"))
            .map(line -> line.split(" ")[0])
            .map(Checksum::new)
            .filter(Checksum::isValid)
            .forEach(checksums::add);



        return checksums;
    }
}
