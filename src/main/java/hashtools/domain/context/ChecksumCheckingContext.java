package hashtools.domain.context;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.strategy.checksumextraction.ChecksumExtraction;
import hashtools.strategy.checksumgeneratorupdater.ChecksumGeneratorUpdate;
import hashtools.strategy.inputidentification.InputIdentification;
import hashtools.strategy.problemdetection.ProblemDetection;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class ChecksumCheckingContext {

    private InputIdentification inputIdentification;
    private ChecksumGeneratorUpdate checksumGeneratorUpdate;
    private ProblemDetection inputProblemDetection;

    private ChecksumExtraction checksumExtraction;
    private ProblemDetection checksumProblemDetection;



    public ChecksumCheckingContext() {
        this.inputIdentification = new InputIdentification() {};
        this.checksumGeneratorUpdate = new ChecksumGeneratorUpdate() {};
        this.inputProblemDetection = new ProblemDetection() {};

        this.checksumExtraction = new ChecksumExtraction() {};
        this.checksumProblemDetection = new ProblemDetection() {};
    }



    public String getIdentification() {
        return inputIdentification.getIdentification();
    }

    public ChecksumGeneratorUpdate.Result updateChecksumGenerators(Collection<ChecksumGenerator> generators) {
        return checksumGeneratorUpdate.updateChecksumGenerators(generators);
    }

    public void cancelChecksumGeneratorsUpdate() {
        checksumGeneratorUpdate.cancelChecksumGeneratorsUpdate();
    }

    public Optional<String> detectProblem() {
        return inputProblemDetection
            .detectProblem()
            .or(checksumProblemDetection::detectProblem);
    }

    public ChecksumExtraction.Result extractOfficialChecksums() {
        return checksumExtraction.extractOfficialChecksums();
    }



    public void setInputIdentification(InputIdentification inputIdentification) {
        this.inputIdentification = Objects.requireNonNull(inputIdentification);
    }

    public void setChecksumGeneratorUpdate(ChecksumGeneratorUpdate checksumGeneratorUpdate) {
        this.checksumGeneratorUpdate = Objects.requireNonNull(checksumGeneratorUpdate);
    }

    public void setInputProblemDetection(ProblemDetection inputProblemDetection) {
        this.inputProblemDetection = Objects.requireNonNull(inputProblemDetection);
    }

    public void setChecksumExtraction(ChecksumExtraction checksumExtraction) {
        this.checksumExtraction = Objects.requireNonNull(checksumExtraction);
    }

    public void setChecksumProblemDetection(ProblemDetection checksumProblemDetection) {
        this.checksumProblemDetection = checksumProblemDetection;
    }
}
