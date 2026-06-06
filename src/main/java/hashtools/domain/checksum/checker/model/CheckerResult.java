package hashtools.domain.checksum.checker.model;

import hashtools.core.checksum.Algorithm;
import hashtools.core.formatter.header.HeaderFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class CheckerResult {

    private List<CheckerChecksum> checksums;
    private String identification;

    private double reliability;
    private boolean reliabilityNeedsCalculation;



    public CheckerResult() {
        this.checksums = new ArrayList<>();
        this.identification = "";

        this.reliability = 0.0;
        this.reliabilityNeedsCalculation = true;
    }



    public void addChecksum(CheckerChecksum checksum) {
        checksums.add(checksum);
        this.invalidateReliability();
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public double getReliability() {
        if (reliabilityNeedsCalculation) {
            this.calculateReliability();
            this.validateReliability();
        }

        return reliability;
    }

    public String formatForConsolePrinting() {
        HeaderFormatter formatter = new HeaderFormatter();
        String[] headers = formatter.alignToLeft(new String[]{
            "Algorithm",
            "Official",
            "Generated",
            "Status of the checking"
        });



        String lineSeparator = System.lineSeparator();
        String separator = "-".repeat(headers[0].length() + Algorithm.SHA512.getLength() + 2);

        StringJoiner result = new StringJoiner(
            lineSeparator + separator + lineSeparator,
            separator + lineSeparator,
            lineSeparator + separator
        );

        for (CheckerChecksum checksum : checksums) {
            result.add(String.format(
                "%s: %s\n%s: %s\n%s: %s\n%s: %s",
                headers[0], checksum.getAlgorithmDisplayName(),
                headers[1], checksum.getOfficialHash(),
                headers[2], checksum.getGeneratedHash(),
                headers[3], checksum.matches()
            ));
        }



        String formattedReliability = String.format(
            "Reliability: %.2f%%",
            this.getReliability() * 100
        );



        return identification
            + lineSeparator
            + result
            + lineSeparator
            + formattedReliability;
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
            .filter(CheckerChecksum::matches)
            .count()
            / checksums.size();

        this.setReliability(reliability);
    }
}
