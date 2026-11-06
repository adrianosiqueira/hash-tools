package hashtools.core.strategy;

import hashtools.core.domain.ProblemException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class FileInputProblemDetection implements ProblemDetection {

    private Path file;



    public FileInputProblemDetection(String filePath) {
        Objects.requireNonNull(filePath);
        this.file = Path.of(filePath);
    }



    @Override
    public void detect()
    throws ProblemException {
        if (Files.notExists(file)) {
            throw new ProblemException("File does not exist");
        }

        if (Files.isDirectory(file)) {
            throw new ProblemException("File is a directory");
        }
    }
}
