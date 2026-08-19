package hashtools.domain.result;

import hashtools.domain.checksum.ComparatorChecksum;

public final class ChecksumComparisonResult {

    private ComparatorChecksum checksum;



    public ChecksumComparisonResult() {
        this.checksum = new ComparatorChecksum();
    }



    public void setChecksum(ComparatorChecksum checksum) {
        this.checksum = checksum;
    }

    public boolean matches() {
        return checksum.matches();
    }
}
