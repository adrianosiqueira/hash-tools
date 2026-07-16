package hashtools.strategy.checksumsource;

import hashtools.domain.checksum.Checksum;

import java.util.List;

public class TextChecksumSource implements ChecksumSource {

    private String text;



    public TextChecksumSource(String text) {
        this.text = text;
    }



    @Override
    public List<Checksum> extractOfficialChecksums() {
        return text
            .lines()
            .map(line -> line.split(" ")[0])
            .map(Checksum::new)
            .filter(Checksum::isValid)
            .toList();
    }
}
