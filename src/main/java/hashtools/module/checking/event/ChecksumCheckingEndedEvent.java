package hashtools.module.checking.event;

import hashtools.core.event.HashToolsEvent;
import hashtools.module.checking.ChecksumCheckingDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChecksumCheckingEndedEvent implements HashToolsEvent {

    private static final double INVALIDATED = -1.0;



    private List<ChecksumCheckingDTO> checksums;
    private double reliability;



    public ChecksumCheckingEndedEvent() {
        this.checksums = new ArrayList<>();
        this.reliability = INVALIDATED;
    }



    public void addChecksum(ChecksumCheckingDTO checksum) {
        ChecksumCheckingDTO c = Optional
            .ofNullable(checksum)
            .orElseGet(ChecksumCheckingDTO::new);

        checksums.add(c);
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
