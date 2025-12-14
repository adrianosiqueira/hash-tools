package hash_tools.domain.request;

import hash_tools.domain.checksum.Algorithm;
import hash_tools.domain.checksum_source.ChecksumSource;
import hash_tools.domain.result.GeneratorResult;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public record GeneratorRequest(
    ChecksumSource checksumSource,
    List<Algorithm> algorithms
) {

    public static GeneratorRequest createUsingSuppliers(Supplier<ChecksumSource> checksumSource, Supplier<List<Algorithm>> algorithms) {
        return new GeneratorRequest(
            checksumSource.get(),
            algorithms.get()
        );
    }


        Stream
            .of(consumers)
            .forEach(result::consume);
    }
}
