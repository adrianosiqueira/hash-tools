package hash_tools.domain.result;

import hash_tools.domain.checksum.Checksum;

import java.util.List;
import java.util.function.Consumer;

public record GeneratorResult(
    List<Checksum> checksums,
    String identification
) {

    public GeneratorResult consume(Consumer<GeneratorResult> consumer) {
        consumer.accept(this);
        return this;
    }
}
