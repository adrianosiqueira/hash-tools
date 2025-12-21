package hash_tools.backend.request.processor;

import hash_tools.backend.checksum.Checksum;
import hash_tools.backend.request.GeneratorRequest;
import hash_tools.backend.result.GeneratorResult;

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
