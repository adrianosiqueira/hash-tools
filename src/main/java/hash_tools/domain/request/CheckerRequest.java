package hash_tools.domain.request;

import hash_tools.domain.checksum_extractor.ChecksumExtractor;
import hash_tools.domain.checksum_source.ChecksumSource;

import java.util.function.Function;
import java.util.function.Supplier;

public record CheckerRequest(
    ChecksumSource checksumSource,
    ChecksumExtractor checksumExtractor
) {

    public static CheckerRequest createUsingSuppliers(Supplier<ChecksumSource> checksumSource, Supplier<ChecksumExtractor> checksumExtractor) {
        return new CheckerRequest(
            checksumSource.get(),
            checksumExtractor.get()
        );
    }



    public <R> R process(Function<CheckerRequest, R> processor) {
        return processor.apply(this);
    }
}
