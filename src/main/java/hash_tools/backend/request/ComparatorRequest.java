package hash_tools.backend.request;

import hash_tools.backend.checksum.Algorithm;
import hash_tools.backend.checksum.source.ChecksumSource;

import java.util.function.Function;
import java.util.function.Supplier;

public class ComparatorRequest {

    private ChecksumSource checksumSource1;
    private ChecksumSource checksumSource2;
    private Algorithm algorithm;



    public <R> R process(Function<ComparatorRequest, R> processor) {
        return processor.apply(this);
    }



    public ChecksumSource checksumSource1() {
        return checksumSource1;
    }

    public ComparatorRequest checksumSource1(Supplier<ChecksumSource> checksumSource1) {
        this.checksumSource1 = checksumSource1.get();
        return this;
    }

    public ChecksumSource checksumSource2() {
        return checksumSource2;
    }

    public ComparatorRequest checksumSource2(Supplier<ChecksumSource> checksumSource2) {
        this.checksumSource2 = checksumSource2.get();
        return this;
    }

    public Algorithm algorithm() {
        return algorithm;
    }

    public ComparatorRequest algorithm(Supplier<Algorithm> algorithm) {
        this.algorithm = algorithm.get();
        return this;
    }
}
