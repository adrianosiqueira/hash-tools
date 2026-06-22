package hashtools.module.checker.domain;

import java.util.ArrayList;
import java.util.List;

public class ChecksumCheckingResult {

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

        return (double) checksums
            .stream()
            .filter(CheckerChecksum::matches)
            .count()
            / checksums.size();
    }



    public List<CheckerChecksum> getChecksums() {
        return checksums;
    }
}
