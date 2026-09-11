package hashtools.domain.result;

import hashtools.domain.checksum.ChecksumPair;

import java.util.ArrayList;
import java.util.List;

public final class ChecksumCheckingResult {

    private List<ChecksumPair> checksums;



    public ChecksumCheckingResult() {
        this.checksums = new ArrayList<>();
    }



    public void addChecksum(ChecksumPair checksum) {
        this.checksums.add(checksum);
    }

    public double calculateReliability() {
        if (checksums.isEmpty()) {
            return 0.0;
        }

        return checksums
            .stream()
            .filter(ChecksumPair::matches)
            .count()
            * 100.0
            / checksums.size();
    }
}
