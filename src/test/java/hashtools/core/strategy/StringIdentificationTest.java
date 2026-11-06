package hashtools.core.strategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringIdentificationTest {

    @Test
    void constructor() {
        Assertions.assertDoesNotThrow(
            () -> new StringIdentification(""),
            "Should not throw an exception when the string is not null"
        );

        Assertions.assertThrows(
            NullPointerException.class,
            () -> new StringIdentification(null),
            "Should throw a NullPointerException when the string is null"
        );
    }



    @Test
    void identify() {
        Assertions.assertEquals(
            "",
            new StringIdentification("").identify(),
            "Should return the same string as provided"
        );

        Assertions.assertNotEquals(
            "1",
            new StringIdentification("").identify(),
            "Should return the different strings"
        );
    }
}
