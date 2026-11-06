package hashtools.core.strategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class StringChecksumGeneratorUpdateTest {

    @Test
    void constructor() {
        Assertions.assertDoesNotThrow(
            () -> new StringChecksumGeneratorUpdate(""),
            "Should not throw an exception when the string is not null"
        );

        Assertions.assertThrows(
            NullPointerException.class,
            () -> new StringChecksumGeneratorUpdate(null),
            "Should throw a NullPointerException when the string is null"
        );
    }



    @Test
    void update() {
        Assertions.assertDoesNotThrow(
            () -> new StringChecksumGeneratorUpdate("").update(List.of()),
            "Should not throw an exception when successful"
        );

        Assertions.assertThrows(
            NullPointerException.class,
            () -> new StringChecksumGeneratorUpdate("").update(null),
            "Should throw a NullPointerException when the generators are null"
        );
    }
}
