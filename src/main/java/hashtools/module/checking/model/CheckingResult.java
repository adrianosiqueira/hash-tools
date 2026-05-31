package hashtools.module.checking.model;

import hashtools.core.model.Algorithm;
import hashtools.core.strategy.checksumidentifier.ChecksumIdentifier;
import hashtools.core.strategy.formatter.HeaderFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

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



    public String formatForConsolePrinting(HeaderFormatter formatter) {
        String[] alignedHeaders = formatter.format(new String[]{
            "Algorithm",
            "Official",
            "Generated",
            "Status do Resultado da verificação"
        });



        String lineSeparator = System.lineSeparator();
        String separator = "-".repeat(alignedHeaders[0].length() + Algorithm.SHA512.getLength() + 2);

        StringJoiner result = new StringJoiner(
            lineSeparator + separator + lineSeparator,
            separator + lineSeparator,
            lineSeparator + separator
        );

        for (CheckingChecksum checksum : checksums) {
            result.add(String.format(
                "%s: %s\n%s: %s\n%s: %s\n%s: %s",
                alignedHeaders[0], checksum.getAlgorithmDisplayName(),
                alignedHeaders[1], checksum.getOfficialHash(),
                alignedHeaders[2], checksum.getGeneratedHash(),
                alignedHeaders[3], checksum.matches()
            ));
        }



        return result.toString();
    }
}
