package hashtools.checker.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class CheckingChecksums {

    private Collection<CheckingChecksum> checksums;



    public CheckingChecksums() {
        this.checksums = new ArrayList<>();
    }



    public void add(CheckingChecksum checksum) {
        Objects.requireNonNull(checksum);
        checksums.add(checksum);
    }

    public Collection<CheckingChecksum> getChecksums() {
        return checksums;
    }
}
