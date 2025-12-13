package hash_tools.domain.request;

import hash_tools.domain.checksum.Algorithm;
import hash_tools.domain.checksum_source.ChecksumSource;
import hash_tools.domain.result.GeneratorResult;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public record GeneratorRequest(
    ChecksumSource checksumSource,
    List<Algorithm> algorithms
) {

    @SafeVarargs
    public final void process(Function<GeneratorRequest, GeneratorResult> processor, Consumer<GeneratorResult>... consumers) {
        GeneratorResult result = processor.apply(this);

        Stream
            .of(consumers)
            .forEach(result::consume);
    }
}
