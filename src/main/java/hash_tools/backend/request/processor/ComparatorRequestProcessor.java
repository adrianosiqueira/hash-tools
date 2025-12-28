package hash_tools.backend.request.processor;

import hash_tools.backend.checksum.Checksum;
import hash_tools.backend.checksum.source.ChecksumSource;
import hash_tools.backend.request.ComparatorRequest;
import hash_tools.backend.result.ComparatorResult;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class ComparatorRequestProcessor implements Function<ComparatorRequest, ComparatorResult> {

    private ComparatorRequest request;



    @Override
    public ComparatorResult apply(ComparatorRequest request) {
        this.request = request;


        List<IdentifiedChecksum> list = this
            .createStreamFromRequest()
            .parallel()
            .map(this::generateChecksum)
            .toList();


        return new ComparatorResult(
            list.getFirst().checksum(),
            list.getFirst().identification(),
            list.getLast().checksum(),
            list.getLast().identification()
        );
    }



    private Stream<ChecksumSource> createStreamFromRequest() {
        return Stream.of(
            request.checksumSource1(),
            request.checksumSource2()
        );
    }

    private IdentifiedChecksum generateChecksum(ChecksumSource source) {
        return new IdentifiedChecksum(
            source.generateChecksum(request.algorithm()),
            source.identify()
        );
    }



    private record IdentifiedChecksum(Checksum checksum, String identification) {}
}
