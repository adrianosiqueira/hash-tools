package hashtools.generator.domain;

import hashtools.core.domain.Checksum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class GenerationChecksums {

    private Collection<Checksum> checksums;



    public GenerationChecksums() {
        this.checksums = new ArrayList<>();
    }



    public void add(Checksum checksum) {
        Objects.requireNonNull(checksum);
        checksums.add(checksum);
    }

    public Collection<Checksum> getChecksums() {
        return checksums;
    }
}
