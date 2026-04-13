package hashtools.core.strategy.checksumextractor;

import hashtools.core.model.Checksum;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class StringChecksumExtractor implements ChecksumExtractor {

    private final String string;



    public StringChecksumExtractor(String string) {
        this.string = Optional
            .ofNullable(string)
            .orElse("");
    }



    @Override
    public List<Checksum> extract() {
        return Stream
            .of(new Checksum(string))
            .filter(Checksum::isValid)
            .toList();
    }
}
