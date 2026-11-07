package hashtools.core.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AlgorithmTest {

    @Test
    void getByLength() {
        Assertions
            .assertThat(Algorithm.getByLength("a".repeat(32)))
            .withFailMessage("Should return a filled optional when the checksum is valid")
            .isPresent();

        Assertions
            .assertThat(Algorithm.getByLength(""))
            .withFailMessage("Should return empty optional when the hash is not valid")
            .isEmpty();

        Assertions
            .assertThatCode(() -> Algorithm.getByLength(null))
            .withFailMessage("Should throw NullPointerException when the hash is null")
            .isInstanceOf(NullPointerException.class);
    }



    @Test
    void createChecksumGenerator() {
        for (var algorithm : Algorithm.values()) {
            Assertions
                .assertThatCode(algorithm::createChecksumGenerator)
                .withFailMessage("Should not throw any exception when creating generator from " + algorithm)
                .doesNotThrowAnyException();
        }
    }
}
