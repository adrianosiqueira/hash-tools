package hashtools.domain.result;

import hashtools.domain.checksum.ComparatorChecksum;
import hashtools.service.ChecksumComparisonService;

public final class ChecksumComparisonResult implements ChecksumComparisonService.Result {

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
}
