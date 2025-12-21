package hash_tools.backend.request;

import hash_tools.backend.checksum.Algorithm;
import hash_tools.backend.checksum.source.ChecksumSource;

import java.util.function.Function;
import java.util.function.Supplier;

public class ComparatorRequest {

    private ChecksumSource checksumSource1;
    private ChecksumSource checksumSource2;
    private Algorithm algorithm;



    private ComparatorRequest(ChecksumSource checksumSource1, ChecksumSource checksumSource2, Algorithm algorithm) {
        this.checksumSource1 = checksumSource1;
        this.checksumSource2 = checksumSource2;
        this.algorithm = algorithm;
    }



    public static ComparatorRequest createUsingSuppliers(Supplier<ChecksumSource> checksumSource1, Supplier<ChecksumSource> checksumSource2, Supplier<Algorithm> algorithm) {
        return new ComparatorRequest(
            checksumSource1.get(),
            checksumSource2.get(),
            algorithm.get()
        );
    }



    public <R> R process(Function<ComparatorRequest, R> processor) {
        return processor.apply(this);
    }



    public ChecksumSource checksumSource1() {
        return checksumSource1;
    }

    public ChecksumSource checksumSource2() {
        return checksumSource2;
    }

    public Algorithm algorithm() {
        return algorithm;
    }
}
