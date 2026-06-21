package hashtools.core.strategy.identification;

import java.nio.file.Path;
import java.util.Objects;

public class FileInputIdentification implements InputIdentification {

    private Path file;



    public FileInputIdentification(String filePath) {
        Objects.requireNonNull(filePath);
        this.file = Path.of(filePath);
    }



    @Override
    public String identify() {
        return file
            .toAbsolutePath()
            .toString();
    }
}
