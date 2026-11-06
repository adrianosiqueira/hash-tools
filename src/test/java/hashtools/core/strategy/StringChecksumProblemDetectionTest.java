package hashtools.core.strategy;

import hashtools.core.domain.ProblemException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringChecksumProblemDetectionTest {

    @Test
    void detect() {
        Assertions.assertDoesNotThrow(
            () -> new StringChecksumProblemDetection("a".repeat(32)).detect(),
            "Should not throw an exception for a valid string"
        );

        Assertions.assertThrows(
            NullPointerException.class,
            () -> new StringChecksumProblemDetection(null).detect(),
            "Should throw a NullPointerException for a null string"
        );

        Assertions.assertThrows(
            ProblemException.class,
            () -> new StringChecksumProblemDetection("").detect(),
            "Should throw a ProblemException for an empty string"
        );
    }
}
