package hashtools.domain.result;

import hashtools.domain.checksum.ChecksumPair;

public final class ChecksumComparisonResult {

    private ChecksumPair checksum;



    public ChecksumComparisonResult() {
        this.checksum = new ChecksumPair();
    }



    public void setChecksum(ChecksumPair checksum) {
        this.checksum = checksum;
    }

    public boolean matches() {
        return checksum.matches();
    }
}
