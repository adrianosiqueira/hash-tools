package hash_tools.domain.request;

import hash_tools.domain.checksum_extractor.ChecksumExtractor;
import hash_tools.domain.checksum_source.ChecksumSource;

import java.util.function.Function;
import java.util.function.Supplier;

public class CheckerRequest {

    private ChecksumSource checksumSource;
    private ChecksumExtractor checksumExtractor;



    private CheckerRequest(ChecksumSource checksumSource, ChecksumExtractor checksumExtractor) {
        this.checksumSource = checksumSource;
        this.checksumExtractor = checksumExtractor;
    }



    public static CheckerRequest createUsingSuppliers(Supplier<ChecksumSource> checksumSource, Supplier<ChecksumExtractor> checksumExtractor) {
        return new CheckerRequest(
            checksumSource.get(),
            checksumExtractor.get()
        );
    }



    public <R> R process(Function<CheckerRequest, R> processor) {
        return processor.apply(this);
    }



    public ChecksumSource checksumSource() {
        return checksumSource;
    }

    public ChecksumExtractor checksumExtractor() {
        return checksumExtractor;
    }
}
