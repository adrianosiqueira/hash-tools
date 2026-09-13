package hashtools.domain.parameter;

import hashtools.strategy.checksumextraction.ChecksumExtraction;
import hashtools.strategy.generatorupdate.GeneratorUpdate;
import hashtools.strategy.problemdetection.ProblemDetection;

import java.util.function.Consumer;

public class ChecksumCheckingParameter {

    private ProblemDetection inputProblemDetection;
    private GeneratorUpdate generatorUpdate;

    private ProblemDetection checksumProblemDetection;
    private ChecksumExtraction checksumExtraction;

    private Consumer<Double> progressTracker;



    public ChecksumCheckingParameter() {
        this.inputProblemDetection = new ProblemDetection() {};
        this.generatorUpdate = new GeneratorUpdate() {};

        this.checksumProblemDetection = new ProblemDetection() {};
        this.checksumExtraction = new ChecksumExtraction() {};

        this.progressTracker = _ -> {};
    }



    public ProblemDetection getInputProblemDetection() {
        return inputProblemDetection;
    }

    public void setInputProblemDetection(ProblemDetection inputProblemDetection) {
        this.inputProblemDetection = inputProblemDetection;
    }

    public GeneratorUpdate getGeneratorUpdate() {
        return generatorUpdate;
    }

    public void setGeneratorUpdate(GeneratorUpdate generatorUpdate) {
        this.generatorUpdate = generatorUpdate;
    }

    public ProblemDetection getChecksumProblemDetection() {
        return checksumProblemDetection;
    }

    public void setChecksumProblemDetection(ProblemDetection checksumProblemDetection) {
        this.checksumProblemDetection = checksumProblemDetection;
    }

    public ChecksumExtraction getChecksumExtraction() {
        return checksumExtraction;
    }

    public void setChecksumExtraction(ChecksumExtraction checksumExtraction) {
        this.checksumExtraction = checksumExtraction;
    }

    public Consumer<Double> getProgressTracker() {
        return progressTracker;
    }

    public void setProgressTracker(Consumer<Double> progressTracker) {
        this.progressTracker = progressTracker;
    }
}
