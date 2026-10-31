package hashtools.core.domain;

import hashtools.core.strategy.ChecksumGeneratorUpdate;
import hashtools.core.strategy.Identification;
import hashtools.core.strategy.ProblemDetection;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

public class InputSource {

    private ProblemDetection problemDetection;
    private Identification identification;
    private ChecksumGeneratorUpdate checksumGeneratorUpdate;



    private InputSource(ProblemDetection problemDetection, Identification identification, ChecksumGeneratorUpdate checksumGeneratorUpdate) {
        this.problemDetection = problemDetection;
        this.identification = identification;
        this.checksumGeneratorUpdate = checksumGeneratorUpdate;
    }



    public static InputSource fileInputSource(String filePath) {
        Objects.nonNull(filePath);

        // TODO Add the real implementation
        return new InputSource(
            null,
            null,
            null
        );
    }

    public static InputSource stringInputSource(String string) {
        Objects.nonNull(string);

        // TODO Add the real implementation
        return new InputSource(
            null,
            null,
            null
        );
    }



    public void detectProblem()
    throws ProblemException {
        problemDetection.detect();
    }

    public String getIdentification() {
        return identification.identify();
    }

    public void updateGenerators(Collection<ChecksumGenerator> generators)
    throws InterruptedException, IOException {
        checksumGeneratorUpdate.update(generators);
    }
}
