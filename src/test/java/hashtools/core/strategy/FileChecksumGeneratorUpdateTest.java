package hashtools.core.strategy;

import hashtools.core.domain.ChecksumGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

class FileChecksumGeneratorUpdateTest {

    @Test
    void constructor() {
        Assertions.assertDoesNotThrow(
            () -> new FileChecksumGeneratorUpdate(""),
            "Should not throw an exception when the string is not null"
        );

        Assertions.assertThrows(
            NullPointerException.class,
            () -> new FileChecksumGeneratorUpdate(null),
            "Should throw a NullPointerException when the string is null"
        );
    }



    @Test
    void update() throws IOException {
        // Setup
        var validFile = Files.createTempFile(null, null);
        Files.writeString(validFile, "Something");

        var directory = Files.createTempDirectory(null);
        var generators = List.<ChecksumGenerator>of();



        Assertions.assertDoesNotThrow(
            () -> new FileChecksumGeneratorUpdate(validFile.toString()).update(generators),
            "Should not throw an exception when the file is valid"
        );

        Assertions.assertThrows(
            IOException.class,
            () -> new FileChecksumGeneratorUpdate(directory.toString()).update(generators),
            "Should throw an IOException when the file is a directory"
        );

        Assertions.assertThrows(
            IOException.class,
            () -> new FileChecksumGeneratorUpdate("/file/not/exist.txt").update(generators),
            "Should throw an IOException when the file does not exist"
        );

        Assertions.assertThrows(
            NullPointerException.class,
            () -> new FileChecksumGeneratorUpdate(validFile.toString()).update(null),
            "Should throw a NullPointerException when the generators list is null"
        );



        // Cleanup
        Files.deleteIfExists(validFile);
        Files.deleteIfExists(directory);
    }
}
