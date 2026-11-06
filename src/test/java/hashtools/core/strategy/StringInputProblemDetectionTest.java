package hashtools.core.strategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringInputProblemDetectionTest {

    @Test
    void constructor() {
        Assertions.assertDoesNotThrow(
            () -> new StringInputProblemDetection(""),
            "Should not throw an exception when the string input is not null"
        );

        Assertions.assertThrows(
            NullPointerException.class,
            () -> new StringInputProblemDetection(null),
            "Should throw a NullPointerException when the string is null"
        );
    }



    @Test
    void detect() {
        Assertions.assertDoesNotThrow(
            () -> new StringInputProblemDetection("").detect(),
            "Should not throw exception when the string is valid"
        );
    }
}
