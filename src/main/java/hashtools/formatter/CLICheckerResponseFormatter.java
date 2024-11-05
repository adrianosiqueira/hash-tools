package hashtools.formatter;

import hashtools.domain.Algorithm;
import hashtools.domain.CheckerChecksum;
import hashtools.domain.CheckerResponse;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CLICheckerResponseFormatter implements Formatter<CheckerResponse> {

    private static final String LAYOUT = """
                                         %s
                                         %s
                                         %s
                                         %s
                                         """;

    private final ResourceBundle language;

    @Override
    public String format(CheckerResponse response) {
        String
            matches = language.getString("hashtools.formatter.cli-checker-response-formatter.matches"),
            notMatches = language.getString("hashtools.formatter.cli-checker-response-formatter.not-matches"),
            reliability = language.getString("hashtools.formatter.cli-checker-response-formatter.reliability"),
            statusMask = "%s: %.2f%%";

        return response
            .getChecksums()
            .stream()
            .sorted(new Ascending())
            .map(checksum -> LAYOUT.formatted(
                checksum.getAlgorithm(),
                checksum.getOfficialHash(),
                checksum.getGeneratedHash(),
                checksum.matches() ? matches : notMatches))
            .collect(Collectors.joining(System.lineSeparator(), "", System.lineSeparator()))
            .concat(statusMask.formatted(reliability, response.calculateReliabilityPercentage()));
    }


    private static class Ascending implements Comparator<CheckerChecksum> {

        @Override
        public int compare(CheckerChecksum c1, CheckerChecksum c2) {
            return Comparator
                .comparing(Algorithm::getLength)
                .compare(c1.getAlgorithm(), c2.getAlgorithm());
        }
    }
}
