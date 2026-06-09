package hashtools.domain.checksum.checker.model;

import hashtools.core.checksum.Algorithm;
import hashtools.core.formatter.header.HeaderFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class CheckerResult {

    private List<CheckerChecksum> checksums;
    private String identification;



    public CheckerResult() {
        this.checksums = new ArrayList<>();
        this.identification = "";
    }



    public void addChecksum(CheckerChecksum checksum) {
        Optional
            .ofNullable(checksum)
            .ifPresent(checksums::add);
    }

    public void setIdentification(String identification) {
        this.identification = Optional
            .ofNullable(identification)
            .orElse("");
    }

    public double getReliability() {
        if (checksums.isEmpty()) {
            return 0.0;
        }


        return (double) checksums
            .stream()
            .filter(CheckerChecksum::matches)
            .count()
            / checksums.size();
    }

    public String formatForConsolePrinting() {
        HeaderFormatter formatter = new HeaderFormatter();
        String[] headers = formatter.alignToLeft(new String[]{
            "Algorithm",
            "Official",
            "Generated",
            "Status"
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
            + formattedReliability
            + lineSeparator;
    }
}
