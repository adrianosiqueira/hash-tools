package hashtools.module.comparison;

import java.util.Optional;

public class ChecksumComparisonResult {

    private ChecksumComparisonDTO checksum1;
    private ChecksumComparisonDTO checksum2;



    public ChecksumComparisonResult() {
        this.checksum1 = new ChecksumComparisonDTO();
        this.checksum2 = new ChecksumComparisonDTO();
    }



    public boolean matches() {
        return checksum1.matches(checksum2);
    }



    public void setChecksum1(ChecksumComparisonDTO checksum1) {
        this.checksum1 = Optional
            .ofNullable(checksum1)
            .orElseGet(ChecksumComparisonDTO::new);
    }

    public void setChecksum2(ChecksumComparisonDTO checksum2) {
        this.checksum2 = Optional
            .ofNullable(checksum2)
            .orElseGet(ChecksumComparisonDTO::new);
    }



    public ChecksumComparisonDTO getChecksum1() {
        return checksum1;
    }

    public ChecksumComparisonDTO getChecksum2() {
        return checksum2;
    }
}
