package hashtools.core.strategy.checksumidentifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Optional;

public class FileChecksumIdentifier implements ChecksumIdentifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileChecksumIdentifier.class);



    private final String filePath;



    public FileChecksumIdentifier(String filePath) {
        this.filePath = Optional
            .ofNullable(filePath)
            .orElse("");
    }



    @Override
    public String getIdentification() {
        String identification = Path
            .of(filePath)
            .toAbsolutePath()
            .toString();

        LOGGER.info("The file was identified as '{}'.", identification);
        return identification;
    }
}
