package hashtools.core.strategy.problem;

import hashtools.core.file.EnhancedFile;
import hashtools.core.file.FileExtension;

import java.util.Optional;

public class ChecksumFileProblemDetection implements ProblemDetection {

    private EnhancedFile file;



    public ChecksumFileProblemDetection(String filePath) {
        this.file = EnhancedFile
            .filePath(filePath)
            .orElseThrow();
    }



    @Override
    public Optional<String> detect() {
        if (!file.hasFileExtension(FileExtension.HASH)) {
            return Optional.of("The file is not a checksum file. Use the dialog selector to select a valid file.");
        } else if (!file.exists()) {
            return Optional.of("The checksum file does not exist. Use the dialog selector to select a valid file.");
        } else if (!file.isRegularFile()) {
            return Optional.of("The checksum file is not a regular file. Use the dialog selector to select a valid file.");
        }

        return Optional.empty();
    }
}
