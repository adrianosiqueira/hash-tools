package hashtools.module.checking.model;

import hashtools.core.strategy.checksumidentifier.ChecksumIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CheckingResult {

    private List<CheckingChecksum> checksums;
    private ChecksumIdentifier identifier;

    private double reliability;
    private boolean reliabilityNeedsCalculation;



    public CheckingResult() {
        this.setChecksums(null);
        this.setIdentifier(null);

        this.setReliability(0.0);
        this.invalidateReliability();
    }



    public void addChecksum(CheckingChecksum checksum) {
        checksums.add(checksum);
        this.invalidateReliability();
    }

    public String getIdentification() throws Exception {
        return identifier.getIdentification();
    }



    public List<CheckingChecksum> getChecksums() {
        return checksums;
    }

    public void setChecksums(List<CheckingChecksum> checksums) {
        this.checksums = Optional
            .ofNullable(checksums)
            .orElseGet(ArrayList::new);

        this.invalidateReliability();
    }

    public ChecksumIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(ChecksumIdentifier identifier) {
        this.identifier = Optional
            .ofNullable(identifier)
            .orElseGet(ChecksumIdentifier::nullImplementation);
    }

    public double getReliability() {
        if (reliabilityNeedsCalculation) {
            this.calculateReliability();
            this.validateReliability();
        }

        return reliability;
    }

    private void setReliability(double reliability) {
        this.reliability = reliability < 0.5
            ? Math.max(reliability, 0.0)
            : Math.min(reliability, 1.0);
    }



    private void validateReliability() {
        this.reliabilityNeedsCalculation = false;
    }

    private void invalidateReliability() {
        this.reliabilityNeedsCalculation = true;
    }

    private void calculateReliability() {
        if (checksums.isEmpty()) {
            this.setReliability(0.0);
            return;
        }

        double reliability = (double) checksums
            .stream()
            .filter(CheckingChecksum::matches)
            .count()
            / checksums.size();

        this.setReliability(reliability);
    }
}
