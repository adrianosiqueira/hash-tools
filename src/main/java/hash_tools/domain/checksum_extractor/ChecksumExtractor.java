package hash_tools.domain.checksum_extractor;

import hash_tools.domain.checksum.Checksum;

import java.util.List;
import java.util.stream.Stream;

public abstract class ChecksumExtractor {

    public List<Checksum> extractOfficialChecksums() {
        return extractData()
            .map(Checksum::fromValue)
            .filter(Checksum::isValid)
            .toList();
    }



    protected abstract Stream<String> extractData();



    protected String extractChecksumPart(String string) {
        return string.split(" ")[0];
    }
}
