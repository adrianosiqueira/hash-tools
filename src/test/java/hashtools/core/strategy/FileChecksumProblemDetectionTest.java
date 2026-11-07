package hashtools.core.strategy;

import hashtools.core.domain.ProblemException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class FileChecksumProblemDetectionTest {

    @Test
    void constructor() {
        Assertions.assertDoesNotThrow(
            () -> new FileChecksumProblemDetection(""),
            "Should not throw an exception when the string is not null"
        );

        Assertions.assertThrows(
            NullPointerException.class,
            () -> new FileChecksumProblemDetection(null),
            "Should throw a NullPointerException when the string is null"
        );
    }



    @Test
    void detect() throws IOException {
        // Setup
        var validFiles = new Path[]{
            Files.createTempFile(null, ".txt"),
            Files.createTempFile(null, ".md5"),
            Files.createTempFile(null, ".sha1"),
            Files.createTempFile(null, ".sha224"),
            Files.createTempFile(null, ".sha256"),
            Files.createTempFile(null, ".sha384"),
            Files.createTempFile(null, ".sha512")
        };

        var mp3File = Files.createTempFile(null, ".mp3");
        var directory = Files.createTempDirectory(null);



        for (var file : validFiles) {
            Assertions.assertDoesNotThrow(
                () -> new FileChecksumProblemDetection(file.toString()).detect(),
                "Should not throw an exception when the file is valid"
            );
        }

        Assertions.assertThrows(
            ProblemException.class,
            () -> new FileChecksumProblemDetection(mp3File.toString()).detect(),
            "Should throw a ProblemException when the file is not a checksum file"
        );

        Assertions.assertThrows(
            ProblemException.class,
            () -> new FileChecksumProblemDetection(directory.toString()).detect(),
            "Should throw a ProblemException when the file is a directory"
        );



        // Cleanup
        for (var file : validFiles) {
            Files.deleteIfExists(file);
        }

        Files.deleteIfExists(mp3File);
        Files.deleteIfExists(directory);
    }
}
