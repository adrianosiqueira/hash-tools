package hashtools.core.strategy.checksumsource;

import hashtools.core.model.Checksum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class StringChecksumSource implements ChecksumSource {

    private String string;



    public StringChecksumSource(String string) {
        this.string = Objects.requireNonNullElse(string, "");
    }



    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public List<Checksum> getValidChecksums() throws IOException {
        List<Checksum> checksums = new ArrayList<>();



        Stream
            .of(string.split("\n"))
            .map(line -> line.split(" ")[0])
            .map(Checksum::new)
            .filter(Checksum::isValid)
            .forEach(checksums::add);



        return checksums;
    }
}
