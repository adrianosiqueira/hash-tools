package hashtools.service;

import hashtools.domain.algorithm.Algorithm;
import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.domain.result.CanceledResult;
import hashtools.domain.result.ChecksumGenerationResult;
import hashtools.domain.result.ExceptionResult;
import hashtools.domain.result.ProblemResult;
import hashtools.strategy.generatorupdate.GeneratorUpdate;
import hashtools.strategy.identification.Identification;
import hashtools.strategy.problemdetection.ProblemDetection;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class ChecksumGenerationService {

    private Consumer<Exception> exceptionConsumer;
    private Consumer<String> problemConsumer;
    private Consumer<Double> progressConsumer;
    private Consumer<ChecksumGenerationResult> resultConsumer;

    private ProblemDetection inputProblemDetection;
    private GeneratorUpdate generatorUpdate;
    private Collection<Algorithm> algorithms;
    private Identification identification;

    private boolean canceled;



    public ChecksumGenerationService() {
        this.initSetup();
    }



    public void generateChecksums() {
        // Problem detection
        if (this.isCanceled()) {
            return;
        }

        String problem = inputProblemDetection
            .detect()
            .orElse(null);

        if (problem != null) {
            problemConsumer.accept(problem);
            return;
        }



        // Data getting
        if (this.isCanceled()) {
            return;
        }

        Collection<ChecksumGenerator> generators = algorithms
            .stream()
            .map(ChecksumGenerator::createFromAlgorithm)
            .toList();



        // Processing
        if (this.isCanceled()) {
            return;
        }

        try {
            generatorUpdate.update(generators, progressConsumer);
        } catch (RuntimeException e) {
            exceptionConsumer.accept(e);
            return;
        }



        // Result collecting
        if (this.isCanceled()) {
            return;
        }

        ChecksumGenerationResult result = new ChecksumGenerationResult();
        result.setIdentification(identification::identify);

        generators
            .stream()
            .map(ChecksumGenerator::decodeIntoChecksum)
            .forEach(result::addChecksum);

        resultConsumer.accept(result);
    }

    public void cancelChecksumGeneration() {
        canceled = true;
    }



    public void initSetup() {
        this.exceptionConsumer = _ -> {};
        this.problemConsumer = _ -> {};
        this.progressConsumer = _ -> {};
        this.resultConsumer = _ -> {};

        this.inputProblemDetection = new ProblemDetection() {};
        this.generatorUpdate = new GeneratorUpdate() {};
        this.algorithms = List.of();
        this.identification = new Identification() {};

        this.canceled = false;
    }

    public void setExceptionConsumer(Consumer<Exception> exceptionConsumer) {
        this.exceptionConsumer = exceptionConsumer;
    }

    public void setProblemConsumer(Consumer<String> problemConsumer) {
        this.problemConsumer = problemConsumer;
    }

    public void setProgressConsumer(Consumer<Double> progressConsumer) {
        this.progressConsumer = progressConsumer;
    }

    public void setResultConsumer(Consumer<ChecksumGenerationResult> resultConsumer) {
        this.resultConsumer = resultConsumer;
    }

    public void setInputProblemDetection(ProblemDetection inputProblemDetection) {
        this.inputProblemDetection = inputProblemDetection;
    }

    public void setGeneratorUpdate(GeneratorUpdate generatorUpdate) {
        this.generatorUpdate = generatorUpdate;
    }

    public void setAlgorithms(Collection<Algorithm> algorithms) {
        this.algorithms = algorithms;
    }

    public void setIdentification(Identification identification) {
        this.identification = identification;
    }



    private boolean isCanceled() {
        return canceled;
    }



    @Deprecated(forRemoval = true)
    public sealed interface Result permits CanceledResult, ExceptionResult, ProblemResult, ChecksumGenerationResult {}
}
