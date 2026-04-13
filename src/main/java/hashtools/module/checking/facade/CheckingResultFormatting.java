package hashtools.module.checking.facade;

import hashtools.core.model.Algorithm;
import hashtools.core.strategy.formatter.HeaderFormatter;
import hashtools.core.strategy.formatter.LeftAlignmentHeaderFormatter;
import hashtools.module.checking.model.CheckingChecksum;
import hashtools.module.checking.model.CheckingResult;

import java.util.List;
import java.util.StringJoiner;

public class CheckingResultFormatting {

    public String format(CheckingResult result) {
        HeaderFormatter formatter = new LeftAlignmentHeaderFormatter('.');

        String[] headers = formatter.format(new String[]{
            "Algorithm",
            "Official",
            "Generated",
            "Status"
        });



        String formattedChecksums = this.formatChecksums(
            headers,
            result.getChecksums()
        );

        String formattedReliability = this.formatReliability(
            result.getReliability()
        );



        return this.joinFormattedContent(
            result.getIdentification(),
            formattedChecksums,
            formattedReliability
        );
    }



    private String formatChecksums(String[] headers, List<CheckingChecksum> checksums) {
        String lineSeparator = System.lineSeparator();
        String checksumSeparator = "-".repeat(headers[0].length() + Algorithm.SHA512.getLength() + 2);

        String checksumTemplate = """
            %s: %s
            %s: %s
            %s: %s
            %s: %s\
            """;

        StringJoiner checksumJoiner = new StringJoiner(
            lineSeparator + checksumSeparator + lineSeparator,
            checksumSeparator + lineSeparator,
            lineSeparator + checksumSeparator
        );

        for (CheckingChecksum checksum : checksums) {
            checksumJoiner.add(checksumTemplate.formatted(
                headers[0],
                checksum.getAlgorithm().getDisplayName(),
                headers[1],
                checksum.getOfficialHash(),
                headers[2],
                checksum.getGeneratedHash(),
                headers[3],
                checksum.matches() ? "Matches" : "Does not match"
            ));
        }

        return checksumJoiner.toString();
    }

    private String formatReliability(double reliability) {
        return String.format(
            "%s: %.2f%%",
            "Reliability",
            reliability * 100
        );
    }

    private String joinFormattedContent(String identification, String formattedChecksums, String formattedReliability) {
        String lineSeparator = System.lineSeparator();

        return identification
            + lineSeparator
            + formattedChecksums
            + lineSeparator
            + formattedReliability;
    }
}
