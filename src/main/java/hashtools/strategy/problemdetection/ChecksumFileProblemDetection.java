package hashtools.strategy.problemdetection;

import hashtools.domain.file.EnhancedFile;
import hashtools.domain.file.FileExtension;

import java.util.Optional;

public class ChecksumFileProblemDetection implements ProblemDetection {

    private EnhancedFile file;



    public ChecksumFileProblemDetection(String filePath) {
        this.file = EnhancedFile
            .filePath(filePath)
            .orElse(null);
    }



    @Override
    public Optional<String> detectProblem() {
        if (!file.hasFileExtension(FileExtension.HASH)) {
            return Optional.of("The file is not a checksum file. Use the dialog selector to select a valid file.");
        } else if (!file.exists()) {
            return Optional.of("The checksum file does not exist. Use the dialog selector to select a valid file.");
        } else if (!file.isRegularFile()) {
            return Optional.of("The checksum file is not a regular file. Use the dialog selector to select a valid file.");
        } else {
            return Optional.empty();
        }
    }
}
