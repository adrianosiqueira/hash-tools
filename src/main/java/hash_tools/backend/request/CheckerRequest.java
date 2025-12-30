package hash_tools.backend.request;

import hash_tools.backend.checksum.extractor.ChecksumExtractor;
import hash_tools.backend.checksum.source.ChecksumSource;

import java.util.function.Function;
import java.util.function.Supplier;

public class CheckerRequest {

    private ChecksumSource checksumSource;
    private ChecksumExtractor checksumExtractor;



    public <R> R process(Function<CheckerRequest, R> processor) {
        return processor.apply(this);
    }



    public ChecksumSource checksumSource() {
        return checksumSource;
    }

    public CheckerRequest checksumSource(Supplier<ChecksumSource> checksumSource) {
        this.checksumSource = checksumSource.get();
        return this;
    }

    public ChecksumExtractor checksumExtractor() {
        return checksumExtractor;
    }

    public CheckerRequest checksumExtractor(Supplier<ChecksumExtractor> checksumExtractor) {
        this.checksumExtractor = checksumExtractor.get();
        return this;
    }
}
