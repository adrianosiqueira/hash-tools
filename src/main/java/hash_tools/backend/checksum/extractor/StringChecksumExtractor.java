package hash_tools.backend.checksum.extractor;

import java.util.stream.Stream;

public class StringChecksumExtractor extends ChecksumExtractor {

    private final String string;



    public StringChecksumExtractor(String string) {
        this.string = string;
    }



    @Override
    protected Stream<String> extractDataStream() {
        return Stream
            .of(string)
            .map(this::retrieveChecksumFromLine);
    }
}
