package hashtools.backend.comparator.domain;

import hashtools.backend.core.checksum.Checksum;

import java.util.Objects;

public class ComparatorChecksum {

    private Checksum checksum1;
    private Checksum checksum2;



    public ComparatorChecksum() {
        this.checksum1 = new Checksum();
        this.checksum2 = new Checksum();
    }



    public boolean matches() {
        return checksum1.matches(checksum2);
    }



    public void setChecksum1(Checksum checksum1) {
        this.checksum1 = Objects.requireNonNull(checksum1);
    }

    public void setChecksum2(Checksum checksum2) {
        this.checksum2 = Objects.requireNonNull(checksum2);
    }

    @Override
    public String toString() {
        return "ComparatorChecksum{" +
            "checksum1=" + checksum1 +
            ", checksum2=" + checksum2 +
            ", matches=" + this.matches() +
            '}';
    }
}
