package hash_tools.domain.result;

import hash_tools.domain.checksum.CheckingChecksum;

import java.util.List;
import java.util.function.Consumer;

public record CheckerResult(
    List<CheckingChecksum> checksums,
    double matchingRatio
) {

    public CheckerResult consume(Consumer<CheckerResult> consumer) {
        consumer.accept(this);
        return this;
    }
}
