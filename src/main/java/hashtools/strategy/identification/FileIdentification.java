package hashtools.strategy.identification;

import hashtools.domain.file.EnhancedFile;

public class FileIdentification implements Identification {

    private EnhancedFile file;



    public FileIdentification(String filePath) {
        this.file = EnhancedFile.createFromFilePath(filePath);
    }



    @Override
    public String identify() {
        return file.getFileName();
    }
}
