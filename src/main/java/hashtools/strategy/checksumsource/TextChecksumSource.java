package hashtools.strategy.checksumsource;

import hashtools.domain.checksum.Checksum;
import hashtools.domain.result.CanceledResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public class TextChecksumSource implements ChecksumSource {

    private String text;
    private boolean canceled;



    public TextChecksumSource(String text) {
        this.text = text;
        this.canceled = false;
    }



    @Override
    public Result extractOfficialChecksums() {
        Collection<Checksum> checksums = new ArrayList<>();

        Stream<String> lines = text.lines();
        lines.forEach(line -> {
            if (this.isCanceled()) {
                return;
            }

            String hash = line.split(" ")[0];
            Checksum checksum = Checksum.createFromHash(hash);

            if (checksum.isValid()) {
                checksums.add(checksum);
            }
        });

        return this.isCanceled()
            ? new CanceledResult()
            : new SuccessResult(checksums);
    }

    @Override
    public void cancelChecksumsExtraction() {
        canceled = true;
    }

    @Override
    public Optional<String> detectProblem() {
        if (text == null) {
            return Optional.of("The checksum text is null");
        } else {
            return Optional.empty();
        }
    }



    private boolean isCanceled() {
        return canceled;
    }
}
