package hashtools.module.comparator.domain;

public class ChecksumComparisonResult {

    private ComparatorChecksum checksum;



    public ChecksumComparisonResult() {
        this.checksum = new ComparatorChecksum();
    }



    public void setChecksum(ComparatorChecksum checksum) {
        this.checksum = checksum;
    }

    public double calculateEquality() {
        return checksum.matches()
            ? 1.0
            : 0.0;
    }



    public ComparatorChecksum getChecksum() {
        return checksum;
    }
}
