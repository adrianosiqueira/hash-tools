package hashtools.strategy.problemdetection;

import hashtools.domain.file.EnhancedFile;

import java.util.Optional;

public record InputFileProblemDetection(
    EnhancedFile file
) implements ProblemDetection {

    public InputFileProblemDetection(String filePath) {
        this(EnhancedFile.createFromFilePath(filePath));
    }



    @Override
    public Optional<String> detectProblem() {
        if (!file.exists()) {
            return Optional.of("The input file does not exist. Use the dialog selector to select a valid file.");
        } else if (!file.isRegularFile()) {
            return Optional.of("The input file is not a regular file. Use the dialog selector to select a valid file.");
        } else {
            return Optional.empty();
        }
    }
}
