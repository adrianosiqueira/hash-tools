package hashtools.strategy.inputidentification;

import hashtools.domain.file.EnhancedFile;

public record InputFileIdentification(
    EnhancedFile file
) implements InputIdentification {

    public InputFileIdentification(String filePath) {
        this(EnhancedFile.createFromFilePath(filePath));
    }



    @Override
    public String getIdentification() {
        return file.getAbsolutePath();
    }
}
