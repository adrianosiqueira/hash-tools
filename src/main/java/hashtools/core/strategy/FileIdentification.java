package hashtools.core.strategy;

import java.nio.file.Path;
import java.util.Objects;

public class FileIdentification implements Identification {

    private Path file;



    public FileIdentification(String filePath) {
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
