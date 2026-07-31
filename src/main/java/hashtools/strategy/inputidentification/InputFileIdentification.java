package hashtools.strategy.inputidentification;

import hashtools.domain.file.EnhancedFile;

public class InputFileIdentification implements InputIdentification {

    private EnhancedFile file;



    public InputFileIdentification(String filePath) {
        this.file = EnhancedFile.createFromFilePath(filePath);
    }



    @Override
    public String getIdentification() {
        return file.getAbsolutePath();
    }
}
