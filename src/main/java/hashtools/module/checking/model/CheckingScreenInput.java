package hashtools.module.checking.model;

import hashtools.view.dialog.FileExtension;

import java.nio.file.Path;
import java.util.Optional;

public class CheckingScreenInput {

    private String input;
    private boolean usingInputFile;

    private String checksum;
    private boolean usingChecksumFile;



    public CheckingScreenInput() {
        this.setInput(null);
        this.setUsingInputFile(false);

        this.setChecksum(null);
        this.setUsingChecksumFile(false);
    }



    public void setInput(String input) {
        this.input = Optional
            .ofNullable(input)
            .orElse("");
    }

    public boolean isUsingInputFile() {
        return usingInputFile;
    }

    public void setUsingInputFile(boolean usingInputFile) {
        this.usingInputFile = usingInputFile;
    }

    public void setChecksum(String checksum) {
        this.checksum = Optional
            .ofNullable(checksum)
            .orElse("");
    }

    public boolean isUsingChecksumFile() {
        return usingChecksumFile;
    }

    public void setUsingChecksumFile(boolean usingChecksumFile) {
        this.usingChecksumFile = usingChecksumFile;
    }



    public Path getInputFile() {
        return Path.of(input);
    }

    public Path getChecksumFile() {
        return Path.of(checksum);
    }

    public boolean checksumFileHasValidExtension() {
        String extension = this.getChecksumFileExtension();
        return FileExtension.HASH.containsExtension(extension);
    }



    private String getChecksumFileExtension() {
        int lastDotIndex = checksum.lastIndexOf('.');

        return lastDotIndex > 0
            ? checksum.substring(lastDotIndex + 1)
            : "";
    }
}
