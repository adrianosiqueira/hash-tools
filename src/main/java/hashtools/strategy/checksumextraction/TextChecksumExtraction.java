package hashtools.strategy.checksumextraction;

import hashtools.domain.checksum.Checksum;

import java.util.Collection;
import java.util.stream.Stream;

public class TextChecksumExtraction implements ChecksumExtraction {

    private String text;



    public TextChecksumExtraction(String text) {
        this.text = text;
    }



    @Override
    public Collection<Checksum> extract() throws RuntimeException {
        try (Stream<String> lines = text.lines()) {
            return lines
                .map(line -> line.split(" ")[0])
                .map(Checksum::createFromHash)
                .filter(Checksum::isValid)
                .toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
