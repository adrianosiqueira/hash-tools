package hash_tools.domain.checksum_extractor;

import hash_tools.domain.checksum.Algorithm;
import hash_tools.domain.checksum.Checksum;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class ChecksumExtractor {

    public List<Checksum> extractOfficialChecksums() {
        return extractData()
            .map(this::mapToChecksum)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
    }



    protected abstract Stream<String> extractData();



    protected Optional<Checksum> mapToChecksum(String checksum) {
        return Algorithm
            .fromLength(checksum.length())
            .map(algorithm -> new Checksum(algorithm, checksum));
    }

    protected String extractChecksumPart(String string) {
        return string.split(" ")[0];
    }
}
