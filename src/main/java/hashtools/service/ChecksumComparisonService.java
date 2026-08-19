package hashtools.service;

import hashtools.domain.algorithm.Algorithm;
import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.domain.checksum.Checksum;
import hashtools.domain.checksum.ComparatorChecksum;
import hashtools.domain.result.CanceledResult;
import hashtools.domain.result.ChecksumComparisonResult;
import hashtools.domain.result.ExceptionResult;
import hashtools.domain.result.ProblemResult;
import hashtools.strategy.generatorupdate.GeneratorUpdate;
import hashtools.strategy.problemdetection.ProblemDetection;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class ChecksumComparisonService {

    private Consumer<Exception> exceptionConsumer;
    private Consumer<String> problemConsumer;
    private Consumer<Double> progressConsumer;
    private Consumer<ChecksumComparisonResult> resultConsumer;

    private ProblemDetection inputProblemDetection1;
    private GeneratorUpdate generatorUpdate1;
    private ProblemDetection inputProblemDetection2;
    private GeneratorUpdate generatorUpdate2;
    private Algorithm algorithm;

    private boolean canceled;



    public ChecksumComparisonService() {
        this.initSetup();
    }



    public void compareChecksums() {
        // Problem detection
        if (this.isCanceled()) {
            return;
        }

        String problem = inputProblemDetection1
            .detect()
            .or(inputProblemDetection2::detect)
            .orElse(null);

        if (problem != null) {
            problemConsumer.accept(problem);
            return;
        }



        // Processing
        if (this.isCanceled()) {
            return;
        }

        Future<Checksum> futureChecksum1 = CompletableFuture.supplyAsync(() -> this.generateChecksum(generatorUpdate1));
        Future<Checksum> futureChecksum2 = CompletableFuture.supplyAsync(() -> this.generateChecksum(generatorUpdate2));



        // Result getting
        if (this.isCanceled()) {
            return;
        }

        ComparatorChecksum checksum = new ComparatorChecksum();

        try {
            checksum.setChecksum1(futureChecksum1.get());
            checksum.setChecksum2(futureChecksum2.get());
        } catch (Exception e) {
            exceptionConsumer.accept(e);
            return;
        }

        ChecksumComparisonResult result = new ChecksumComparisonResult();
        result.setChecksum(checksum);

        resultConsumer.accept(result);
    }

    public void cancelChecksumsComparison() {
        canceled = true;
    }



    private Checksum generateChecksum(GeneratorUpdate generatorUpdate) {
        ChecksumGenerator generator = ChecksumGenerator.createFromAlgorithm(algorithm);
        generatorUpdate.update(List.of(generator), progressConsumer);
        return generator.decodeIntoChecksum();
    }



    public void initSetup() {
        this.exceptionConsumer = _ -> {};
        this.problemConsumer = _ -> {};
        this.progressConsumer = _ -> {};
        this.resultConsumer = _ -> {};

        this.inputProblemDetection1 = new ProblemDetection() {};
        this.generatorUpdate1 = new GeneratorUpdate() {};
        this.inputProblemDetection2 = new ProblemDetection() {};
        this.generatorUpdate2 = new GeneratorUpdate() {};
        this.algorithm = Algorithm.MD5;

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

    public void setResultConsumer(Consumer<ChecksumComparisonResult> resultConsumer) {
        this.resultConsumer = resultConsumer;
    }

    public void setInputProblemDetection1(ProblemDetection inputProblemDetection1) {
        this.inputProblemDetection1 = inputProblemDetection1;
    }

    public void setGeneratorUpdate1(GeneratorUpdate generatorUpdate1) {
        this.generatorUpdate1 = generatorUpdate1;
    }

    public void setInputProblemDetection2(ProblemDetection inputProblemDetection2) {
        this.inputProblemDetection2 = inputProblemDetection2;
    }

    public void setGeneratorUpdate2(GeneratorUpdate generatorUpdate2) {
        this.generatorUpdate2 = generatorUpdate2;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }



    private boolean isCanceled() {
        return canceled;
    }



    @Deprecated(forRemoval = true)
    public sealed interface Result permits CanceledResult, ExceptionResult, ProblemResult, ChecksumComparisonResult {}
}
