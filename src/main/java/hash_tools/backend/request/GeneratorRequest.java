package hash_tools.backend.request;

import hash_tools.backend.checksum.Algorithm;
import hash_tools.backend.checksum.checksum_source.ChecksumSource;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class GeneratorRequest {

    private ChecksumSource checksumSource;
    private List<Algorithm> algorithms;



    private GeneratorRequest(ChecksumSource checksumSource, List<Algorithm> algorithms) {
        this.checksumSource = checksumSource;
        this.algorithms = algorithms;
    }



    public static GeneratorRequest createUsingSuppliers(Supplier<ChecksumSource> checksumSource, Supplier<List<Algorithm>> algorithms) {
        return new GeneratorRequest(
            checksumSource.get(),
            algorithms.get()
        );
    }



    public <R> R process(Function<GeneratorRequest, R> processor) {
        return processor.apply(this);
    }



    public ChecksumSource checksumSource() {
        return checksumSource;
    }

    public List<Algorithm> algorithms() {
        return algorithms;
    }
}
