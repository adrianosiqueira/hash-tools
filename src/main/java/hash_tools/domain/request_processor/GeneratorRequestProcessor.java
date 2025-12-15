package hash_tools.domain.request_processor;

import hash_tools.domain.checksum.Checksum;
import hash_tools.domain.request.GeneratorRequest;
import hash_tools.domain.result.GeneratorResult;

import java.util.List;
import java.util.function.Function;

public class GeneratorRequestProcessor implements Function<GeneratorRequest, GeneratorResult> {

    @Override
    public GeneratorResult apply(GeneratorRequest request) {
        String identification = request
            .checksumSource()
            .identify();

        List<Checksum> checksums = request
            .algorithms()
            .parallelStream()
            .map(request.checksumSource()::generateChecksum)
            .toList();


        return new GeneratorResult(
            checksums,
            identification
        );
    }
}
