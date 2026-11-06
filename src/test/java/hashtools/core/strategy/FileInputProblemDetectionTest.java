package hashtools.core.strategy;

import hashtools.core.domain.ProblemException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;

class FileInputProblemDetectionTest {

    @Test
    void constructor() {
        Assertions.assertDoesNotThrow(
            () -> new FileInputProblemDetection(""),
            "Should not throw an exception when the string is not null"
        );

        Assertions.assertThrows(
            NullPointerException.class,
            () -> new FileInputProblemDetection(null),
            "Should throw a NullPointerException when the string is null"
        );
    }



    @Test
    void detect() throws IOException {
        // Setup
        var fileThatExists = Files.createTempFile(null, null);
        var directory = Files.createTempDirectory(null);



        Assertions.assertDoesNotThrow(
            () -> new FileInputProblemDetection(fileThatExists.toString()).detect(),
            "Should not throw an exception when the file is a existent file"
        );

        Assertions.assertThrows(
            ProblemException.class,
            () -> new FileInputProblemDetection(directory.toString()).detect(),
            "Should throw a ProblemException when the file is a directory"
        );

        Assertions.assertThrows(
            ProblemException.class,
            () -> new FileInputProblemDetection("/file/not/exists.notfound").detect(),
            "Should throw a ProblemException when the file does not exist"
        );



        // Cleanup
        Files.deleteIfExists(fileThatExists);
        Files.deleteIfExists(directory);
    }
}
