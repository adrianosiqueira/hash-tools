package hashtools.core.strategy;

import hashtools.core.domain.ProblemException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringChecksumProblemDetectionTest {

    @Test
    void constructor() {
        Assertions.assertDoesNotThrow(
            () -> new StringChecksumProblemDetection(""),
            "Should not throw an exception when the string is not null"
        );

        Assertions.assertThrows(
            NullPointerException.class,
            () -> new StringChecksumProblemDetection(null),
            "Should throw a NullPointerException when the string is null"
        );
    }



    @Test
    void detect() {
        Assertions.assertDoesNotThrow(
            () -> new StringChecksumProblemDetection("a".repeat(32)).detect(),
            "Should not throw an exception when the string is valid"
        );

        Assertions.assertThrows(
            ProblemException.class,
            () -> new StringChecksumProblemDetection("").detect(),
            "Should throw a ProblemException when the string is empty"
        );
    }
}
