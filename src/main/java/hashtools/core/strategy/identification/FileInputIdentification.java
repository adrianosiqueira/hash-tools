package hashtools.core.strategy.identification;

import hashtools.core.file.EnhancedFile;

import java.util.Objects;

public class FileInputIdentification implements InputIdentification {

    private EnhancedFile file;



    public FileInputIdentification(String filePath) {
        Objects.requireNonNull(filePath);
        this.file = new EnhancedFile(filePath);
    }



    @Override
    public String identify() {
        return file.getAbsolutePath();
    }
}
