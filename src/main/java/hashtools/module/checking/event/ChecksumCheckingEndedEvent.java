package hashtools.module.checking.event;

import hashtools.core.event.HashToolsEvent;
import hashtools.core.strategy.checksumidentifier.ChecksumIdentifier;
import hashtools.module.checking.model.CheckingChecksum;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChecksumCheckingEndedEvent implements HashToolsEvent {

    private static final double INVALIDATED = -1.0;



    private ChecksumIdentifier identifier;
    private List<CheckingChecksum> checksums;
    private double reliability;



    public ChecksumCheckingEndedEvent() {
        this.checksums = new ArrayList<>();
        this.reliability = INVALIDATED;
    }



    public void setIdentifier(ChecksumIdentifier identifier) {
        this.identifier = identifier;
    }

    public String getIdentification() {
        return identifier.getIdentification();
    }



    public void addChecksum(CheckingChecksum checksum) {
        CheckingChecksum c = Optional
            .ofNullable(checksum)
            .orElseGet(CheckingChecksum::new);

        checksums.add(c);
        reliability = INVALIDATED;
    }

    public List<CheckingChecksum> getChecksums() {
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
            .filter(CheckingChecksum::matches)
            .count()
            / checksums.size();
    }
}
