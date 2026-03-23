package hashtools.module.checking;

import java.util.ArrayList;
import java.util.List;

public class ChecksumCheckingResult {

    private static final double INVALIDATED = -1.0;



    private List<ChecksumCheckingDTO> checksums;
    private double reliability;



    public ChecksumCheckingResult() {
        this.checksums = new ArrayList<>();
        this.reliability = INVALIDATED;
    }



    public void addChecksum(ChecksumCheckingDTO checksum) {
        checksums.add(checksum);
        reliability = INVALIDATED;
    }

    public List<ChecksumCheckingDTO> getChecksums() {
        return checksums;
    }

    public double getReliability() {
        if (reliability == INVALIDATED) {
            reliability = this.calculateReliability();
        }

        return reliability;
    }



    private double calculateReliability() {
        if (checksums.isEmpty()) {
            return 0.0;
        }

        return (double) checksums
            .stream()
            .filter(ChecksumCheckingDTO::matches)
            .count()
            / checksums.size();
    }
}
