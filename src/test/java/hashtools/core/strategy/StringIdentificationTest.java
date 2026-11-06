package hashtools.core.strategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringIdentificationTest {

    private static void execute() {new StringIdentification(null).identify();}

    @Test
    void identify() {
        Assertions.assertEquals(
            "",
            new StringIdentification("").identify(),
            "Should return the same string"
        );

        Assertions.assertNotEquals(
            "1",
            new StringIdentification("").identify(),
            "Should return the different strings"
        );

        Assertions.assertThrows(
            NullPointerException.class,
            () -> new StringIdentification(null),
            "Should throw NullPointerException when string is null"
        );
    }
}
