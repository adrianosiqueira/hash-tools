package hash_tools.domain.checksum_extractor;

import java.util.stream.Stream;

public class StringChecksumExtractor extends ChecksumExtractor {

    private final String string;



    public StringChecksumExtractor(String string) {
        this.string = string;
    }



    @Override
    protected Stream<String> extractData() {
        return Stream
            .of(string)
            .map(this::extractChecksumPart);
    }
}
