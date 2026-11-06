package hashtools.core.strategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringInputProblemDetectionTest {

    @Test
    void detect() {
        Assertions.assertDoesNotThrow(
            () -> new StringInputProblemDetection("").detect(),
            "Should not throw exception when input is valid"
        );

        Assertions.assertThrows(
            NullPointerException.class,
            () -> new StringInputProblemDetection(null),
            "Should throw NullPointerException when input is null"
        );
    }
}
