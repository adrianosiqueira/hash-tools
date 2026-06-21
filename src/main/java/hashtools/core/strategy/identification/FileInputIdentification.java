package hashtools.core.strategy.identification;

import java.nio.file.Path;
import java.util.Objects;

public class FileInputIdentification implements InputIdentification {

    private String filePath;



    public FileInputIdentification(String filePath) {
        this.filePath = Objects.requireNonNull(filePath);
    }



    @Override
    public String identify() {
        return Path
            .of(filePath)
            .toAbsolutePath()
            .toString();
    }
}
