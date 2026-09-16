package hashtools.domain.parameter;

import hashtools.domain.checksum.Algorithm;
import hashtools.strategy.generatorupdate.GeneratorUpdate;
import hashtools.strategy.identification.Identification;
import hashtools.strategy.problemdetection.ProblemDetection;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class ChecksumGenerationParameter {

    private GeneratorUpdate generatorUpdate;
    private Identification identification;
    private ProblemDetection inputProblemDetection;

    private Collection<Algorithm> algorithms;

    private Consumer<Double> progressTracker;



    public ChecksumGenerationParameter() {
        this.generatorUpdate = new GeneratorUpdate() {};
        this.identification = new Identification() {};
        this.inputProblemDetection = new ProblemDetection() {};

        this.algorithms = List.of();

        this.progressTracker = _ -> {};
    }



    public GeneratorUpdate getGeneratorUpdate() {
        return generatorUpdate;
    }

    public void setGeneratorUpdate(GeneratorUpdate generatorUpdate) {
        this.generatorUpdate = generatorUpdate;
    }

    public Identification getIdentification() {
        return identification;
    }

    public void setIdentification(Identification identification) {
        this.identification = identification;
    }

    public ProblemDetection getInputProblemDetection() {
        return inputProblemDetection;
    }

    public void setInputProblemDetection(ProblemDetection inputProblemDetection) {
        this.inputProblemDetection = inputProblemDetection;
    }

    public Collection<Algorithm> getAlgorithms() {
        return algorithms;
    }

    public void setAlgorithms(Collection<Algorithm> algorithms) {
        this.algorithms = algorithms;
    }

    public Consumer<Double> getProgressTracker() {
        return progressTracker;
    }

    public void setProgressTracker(Consumer<Double> progressTracker) {
        this.progressTracker = progressTracker;
    }
}
