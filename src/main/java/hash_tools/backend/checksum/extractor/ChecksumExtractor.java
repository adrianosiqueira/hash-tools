package hash_tools.backend.checksum.extractor;

import hash_tools.backend.checksum.Checksum;

import java.util.List;
import java.util.stream.Stream;

public abstract class ChecksumExtractor {

    public List<Checksum> extractOfficialChecksums() {
        return this
            .extractDataStream()
            .map(Checksum::fromValue)
            .filter(Checksum::valid)
            .toList();
    }



    protected abstract Stream<String> extractDataStream();



    protected String retrieveChecksumFromLine(String line) {
        return line.split(" ")[0];
    }
}
