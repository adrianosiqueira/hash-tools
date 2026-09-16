package hashtools.domain.parameter;

import hashtools.domain.checksum.Algorithm;
import hashtools.strategy.generatorupdate.GeneratorUpdate;
import hashtools.strategy.problemdetection.ProblemDetection;

import java.util.function.Consumer;

public class ChecksumComparisonParameter {

    private ProblemDetection inputProblemDetection1;
    private GeneratorUpdate generatorUpdate1;

    private ProblemDetection inputProblemDetection2;
    private GeneratorUpdate generatorUpdate2;

    private Algorithm algorithm;

    private Consumer<Double> progressTracker;



    public ChecksumComparisonParameter() {
        this.inputProblemDetection1 = new ProblemDetection() {};
        this.generatorUpdate1 = new GeneratorUpdate() {};

        this.inputProblemDetection2 = new ProblemDetection() {};
        this.generatorUpdate2 = new GeneratorUpdate() {};

        this.algorithm = Algorithm.MD5;

        this.progressTracker = _ -> {};
    }



    public ProblemDetection getInputProblemDetection1() {
        return inputProblemDetection1;
    }

    public void setInputProblemDetection1(ProblemDetection inputProblemDetection1) {
        this.inputProblemDetection1 = inputProblemDetection1;
    }

    public GeneratorUpdate getGeneratorUpdate1() {
        return generatorUpdate1;
    }

    public void setGeneratorUpdate1(GeneratorUpdate generatorUpdate1) {
        this.generatorUpdate1 = generatorUpdate1;
    }

    public ProblemDetection getInputProblemDetection2() {
        return inputProblemDetection2;
    }

    public void setInputProblemDetection2(ProblemDetection inputProblemDetection2) {
        this.inputProblemDetection2 = inputProblemDetection2;
    }

    public GeneratorUpdate getGeneratorUpdate2() {
        return generatorUpdate2;
    }

    public void setGeneratorUpdate2(GeneratorUpdate generatorUpdate2) {
        this.generatorUpdate2 = generatorUpdate2;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public Consumer<Double> getProgressTracker() {
        return progressTracker;
    }

    public void setProgressTracker(Consumer<Double> progressTracker) {
        this.progressTracker = progressTracker;
    }
}
