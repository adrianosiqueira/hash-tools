package hashtools.backend.core.strategy.identification;

import hashtools.backend.core.file.EnhancedFile;

public class FileInputIdentification implements InputIdentification {

    private EnhancedFile file;



    public FileInputIdentification(String filePath) {
        this.file = EnhancedFile
            .filePath(filePath)
            .orElseThrow();
    }



    @Override
    public String identify() {
        return file.getAbsolutePath();
    }
}
