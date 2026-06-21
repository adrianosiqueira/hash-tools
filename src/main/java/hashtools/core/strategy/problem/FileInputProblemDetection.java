package hashtools.core.strategy.problem;

import hashtools.core.file.EnhancedFile;

import java.util.Objects;
import java.util.Optional;

public class FileInputProblemDetection implements ProblemDetection {

    private String filePath;



    public FileInputProblemDetection(String filePath) {
        this.filePath = Objects.requireNonNull(filePath);
    }



    @Override
    public Optional<String> detect() {
        EnhancedFile file = new EnhancedFile(filePath);

        if (!file.exists()) {
            return Optional.of("The input file does not exist. Use the dialog selector to select a valid file.");
        } else if (!file.isRegularFile()) {
            return Optional.of("The input file is not a regular file. Use the dialog selector to select a valid file.");
        }

        return Optional.empty();
    }
}
