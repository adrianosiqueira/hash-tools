package hash_tools.backend.request;

import hash_tools.backend.checksum.Algorithm;
import hash_tools.backend.checksum.source.ChecksumSource;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class GeneratorRequest {

    private ChecksumSource checksumSource;
    private List<Algorithm> algorithms;



    public <R> R process(Function<GeneratorRequest, R> processor) {
        return processor.apply(this);
    }



    public ChecksumSource checksumSource() {
        return checksumSource;
    }

    public GeneratorRequest checksumSource(Supplier<ChecksumSource> checksumSource) {
        this.checksumSource = checksumSource.get();
        return this;
    }

    public List<Algorithm> algorithms() {
        return algorithms;
    }

    public GeneratorRequest algorithms(Supplier<List<Algorithm>> algorithms) {
        this.algorithms = algorithms.get();
        return this;
    }
}
