package hashtools.checker.domain;

import hashtools.core.domain.Checksum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class OfficialChecksums {

    private Collection<Checksum> checksums;



    public OfficialChecksums() {
        this.checksums = new ArrayList<>();
    }



    public void add(String hash) {
        Objects.requireNonNull(hash);
        var checksum = new Checksum(hash);

        if (checksum.isValid()) {
            checksums.add(checksum);
        }
    }

    public Collection<Checksum> getChecksums() {
        return checksums;
    }
}
