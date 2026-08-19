package hashtools.domain.result;

import hashtools.domain.checksum.CheckerChecksum;

import java.util.ArrayList;
import java.util.List;

public final class ChecksumCheckingResult {

    private List<CheckerChecksum> checksums;



    public ChecksumCheckingResult() {
        this.checksums = new ArrayList<>();
    }



    public void addChecksum(CheckerChecksum checksum) {
        this.checksums.add(checksum);
    }

    public double calculateReliability() {
        if (checksums.isEmpty()) {
            return 0.0;
        }

        return checksums
            .stream()
            .filter(CheckerChecksum::matches)
            .count()
            * 100.0
            / checksums.size();
    }
}
