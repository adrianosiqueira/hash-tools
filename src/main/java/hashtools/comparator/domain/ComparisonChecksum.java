package hashtools.comparator.domain;

import hashtools.core.domain.Checksum;

import java.util.Objects;

public class ComparisonChecksum {

    private Checksum checksum1;
    private Checksum checksum2;



    public ComparisonChecksum() {
        this.checksum1 = new Checksum();
        this.checksum2 = new Checksum();
    }



    public boolean matches() {
        return checksum1.matches(checksum2);
    }

    public Checksum getChecksum1() {
        return checksum1;
    }

    public void setChecksum1(Checksum checksum1) {
        this.checksum1 = Objects.requireNonNull(checksum1);
    }

    public Checksum getChecksum2() {
        return checksum2;
    }

    public void setChecksum2(Checksum checksum2) {
        this.checksum2 = Objects.requireNonNull(checksum2);
    }
}
