package hashtools.backend.core.strategy.problem;

import hashtools.backend.core.file.EnhancedFile;

import java.util.Optional;

public class InputFileProblemDetection implements ProblemDetection {

    private EnhancedFile file;



    public InputFileProblemDetection(String filePath) {
        this.file = EnhancedFile
            .filePath(filePath)
            .orElseThrow();
    }



    @Override
    public Optional<String> detect() {
        if (!file.exists()) {
            return Optional.of("The input file does not exist. Use the dialog selector to select a valid file.");
        } else if (!file.isRegularFile()) {
            return Optional.of("The input file is not a regular file. Use the dialog selector to select a valid file.");
        }

        return Optional.empty();
    }
}
