package hashtools.core.strategy.problem;

import hashtools.core.file.EnhancedFile;
import hashtools.core.file.FileExtension;

import java.util.Objects;
import java.util.Optional;

public class ChecksumFileProblemDetection implements ProblemDetection {

    private String filePath;



    public ChecksumFileProblemDetection(String filePath) {
        this.filePath = Objects.requireNonNull(filePath);
    }



    @Override
    public Optional<String> detect() {
        EnhancedFile file = new EnhancedFile(filePath);

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
