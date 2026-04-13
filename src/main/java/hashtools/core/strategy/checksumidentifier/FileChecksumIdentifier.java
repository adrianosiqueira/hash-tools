package hashtools.core.strategy.checksumidentifier;

import java.nio.file.Path;
import java.util.Optional;

public class FileChecksumIdentifier implements ChecksumIdentifier {

    private final String filePath;



    public FileChecksumIdentifier(String filePath) {
        this.filePath = Optional
            .ofNullable(filePath)
            .orElse("");
    }



    @Override
    public String getIdentification() {
        return Path
            .of(filePath)
            .toAbsolutePath()
            .toString();
    }
}
