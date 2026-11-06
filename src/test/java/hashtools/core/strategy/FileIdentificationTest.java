package hashtools.core.strategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FileIdentificationTest {

    @Test
    void constructor() {
        Assertions.assertDoesNotThrow(
            ()->new FileIdentification(""),
            "Should not throw an exception when the string is not null"
        );

        Assertions.assertThrows(
            NullPointerException.class,
            ()->new FileIdentification(null),
            "Should throw a NullPointerException when the string is null"
        );
    }



    @Test
    void identify() {
        Assertions.assertEquals(
            "/path/to/file",
            new FileIdentification("/path/to/file").identify(),
            "Should return the absolute path of the file"
        );

        Assertions.assertNotEquals(
            "path/to/file",
            new FileIdentification("path/to/file").identify(),
            "The output is different when the path is relative"
        );
    }
}
