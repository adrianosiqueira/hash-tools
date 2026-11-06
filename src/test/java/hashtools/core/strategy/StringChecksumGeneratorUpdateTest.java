package hashtools.core.strategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class StringChecksumGeneratorUpdateTest {

    @Test
    void update() {
        Assertions.assertDoesNotThrow(
            () -> new StringChecksumGeneratorUpdate("").update(List.of()),
            "Should not throw a exception when successful"
        );

        Assertions.assertThrows(
            NullPointerException.class,
            () -> new StringChecksumGeneratorUpdate(null),
            "Should throw a NullPointerException when the input is null"
        );

        Assertions.assertThrows(
            NullPointerException.class,
            () -> new StringChecksumGeneratorUpdate("").update(null),
            "Should throw a NullPointerException when the generators are null"
        );
    }
}
