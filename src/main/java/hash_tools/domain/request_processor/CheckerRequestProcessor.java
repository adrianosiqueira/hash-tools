package hash_tools.domain.request_processor;

import hash_tools.domain.checksum.CheckingChecksum;
import hash_tools.domain.checksum.Checksum;
import hash_tools.domain.request.CheckerRequest;
import hash_tools.domain.result.CheckerResult;

import java.util.List;
import java.util.function.Function;

public class CheckerRequestProcessor implements Function<CheckerRequest, CheckerResult> {

    @Override
    public CheckerResult apply(CheckerRequest request) {
        List<CheckingChecksum> checksums = request
            .checksumExtractor()
            .extractOfficialChecksums()
            .parallelStream()
            .map(checksum -> generateChecksum(checksum, request))
            .toList();

        double matchingRatio = calculateMatchingRatio(checksums);


        return new CheckerResult(
            checksums,
            matchingRatio
        );
    }



    private CheckingChecksum generateChecksum(Checksum checksum, CheckerRequest request) {
        Checksum generated = request
            .checksumSource()
            .generateChecksum(checksum.algorithm());


        return new CheckingChecksum(
            checksum,
            generated
        );
    }

    private double calculateMatchingRatio(List<CheckingChecksum> checksums) {
        if (checksums.isEmpty()) {
            return 0.0;
        }


        return (double) checksums
            .stream()
            .filter(CheckingChecksum::matches)
            .count()
            / checksums.size();
    }
}
