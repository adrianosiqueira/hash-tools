package hashtools.strategy.checksumextraction;

import hashtools.domain.checksum.Checksum;

import java.util.Collection;
import java.util.stream.Stream;

public record TextChecksumExtraction(
    String text
) implements ChecksumExtraction {

    @Override
    public Result extractOfficialChecksums() {
        try (Stream<String> lines = text.lines()) {
            Collection<Checksum> checksums = lines
                .map(line -> line.split(" ")[0])
                .map(Checksum::createFromHash)
                .filter(Checksum::isValid)
                .toList();

            return new Result.Success(checksums);
        } catch (Exception e) {
            return new Result.Failure(e);
        }
    }
}
