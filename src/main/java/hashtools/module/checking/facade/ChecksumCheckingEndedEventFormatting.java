package hashtools.module.checking.facade;

import hashtools.core.model.Algorithm;
import hashtools.core.strategy.formatter.HeaderFormatter;
import hashtools.core.strategy.formatter.LeftAlignmentHeaderFormatter;
import hashtools.module.checking.event.ChecksumCheckingEndedEvent;
import hashtools.module.checking.event.ChecksumCheckingFormattedEvent;
import hashtools.module.checking.model.CheckingChecksum;

import java.util.List;
import java.util.StringJoiner;

public class ChecksumCheckingEndedEventFormatting {

    public ChecksumCheckingFormattedEvent format(ChecksumCheckingEndedEvent event) {
        HeaderFormatter formatter = new LeftAlignmentHeaderFormatter('.');
        String[] headers = formatter.format(new String[]{
            "Algorithm",
            "Official",
            "Generated",
            "Status"
        });



        String formattedChecksums = this.formatChecksums(
            headers,
            event.getChecksums()
        );

        String formattedReliability = this.formatReliability(
            event.getReliability()
        );

        String content = this.joinFormattedContent(
            event.getIdentification(),
            formattedChecksums,
            formattedReliability
        );



        ChecksumCheckingFormattedEvent formattedEvent = new ChecksumCheckingFormattedEvent();
        formattedEvent.setFormattedContent(content);

        return formattedEvent;
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
            checksumSeparator,
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
