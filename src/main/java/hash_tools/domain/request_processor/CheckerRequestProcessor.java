package hash_tools.domain.request_processor;

import hash_tools.domain.checksum.CheckingChecksum;
import hash_tools.domain.checksum.Checksum;
import hash_tools.domain.request.CheckerRequest;
import hash_tools.domain.result.CheckerResult;

import java.util.List;
import java.util.function.Function;

public class CheckerRequestProcessor implements Function<CheckerRequest, CheckerResult> {

    private CheckerRequest request;



    @Override
    public CheckerResult apply(CheckerRequest request) {
        this.request = request;

        List<CheckingChecksum> checksums = request
            .checksumExtractor()
            .extractOfficialChecksums()
            .parallelStream()
            .map(this::generateChecksum)
            .toList();

        double matchingRatio = calculateMatchingRatio(checksums);


        return new CheckerResult(
            checksums,
            matchingRatio
        );
    }



    private CheckingChecksum generateChecksum(Checksum checksum) {
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
