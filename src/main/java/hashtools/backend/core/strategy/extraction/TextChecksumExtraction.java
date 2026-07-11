package hashtools.backend.core.strategy.extraction;

import hashtools.backend.core.checksum.Checksum;

import java.util.List;
import java.util.Objects;

public class TextChecksumExtraction implements ChecksumExtraction {

    private String text;



    public TextChecksumExtraction(String text) {
        this.text = Objects.requireNonNull(text);
    }



    @Override
    public List<Checksum> extract() throws RuntimeException {
        return text
            .lines()
            .map(line -> line.split(" ")[0])
            .map(Checksum::new)
            .filter(Checksum::isValid)
            .toList();
    }
}
