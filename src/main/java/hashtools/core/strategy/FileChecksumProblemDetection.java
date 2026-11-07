package hashtools.core.strategy;

import hashtools.core.domain.ProblemException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class FileChecksumProblemDetection implements ProblemDetection {

    private Path file;



    public FileChecksumProblemDetection(String filePath) {
        Objects.requireNonNull(filePath);
        this.file = Path.of(filePath);
    }



    @Override
    public void detect()
    throws ProblemException {
        var hasValidExtension = file
            .getFileName()
            .toString()
            .matches("\\w+\\.(txt|md5|sha1|sha224|sha256|sha384|sha512)$");

        if (!hasValidExtension) {
            throw new ProblemException("File is not a checksum file");
        }



        if (Files.notExists(file)) {
            throw new ProblemException("File does not exist");
        }

        if (Files.isDirectory(file)) {
            throw new ProblemException("File is a directory");
        }
    }
}
