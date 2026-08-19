package hashtools.service;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.domain.checksum.CheckerChecksum;
import hashtools.domain.checksum.Checksum;
import hashtools.domain.result.ChecksumCheckingResult;
import hashtools.strategy.checksumextraction.ChecksumExtraction;
import hashtools.strategy.generatorupdate.GeneratorUpdate;
import hashtools.strategy.problemdetection.ProblemDetection;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class ChecksumCheckingService {

    private Consumer<Exception> exceptionConsumer;
    private Consumer<String> problemConsumer;
    private Consumer<Double> progressConsumer;
    private Consumer<ChecksumCheckingResult> resultConsumer;

    private ProblemDetection inputProblemDetection;
    private GeneratorUpdate generatorUpdate;
    private ProblemDetection checksumProblemDetection;
    private ChecksumExtraction checksumExtraction;

    private boolean canceled;



    public ChecksumCheckingService() {
        this.initSetup();
    }



    public void checkChecksums() {
        // Problem detection
        if (this.isCanceled()) {
            return;
        }

        String problem = inputProblemDetection
            .detect()
            .or(checksumProblemDetection::detect)
            .orElse(null);

        if (problem != null) {
            problemConsumer.accept(problem);
            return;
        }



        // Data getting
        if (this.isCanceled()) {
            return;
        }

        Collection<ChecksumWithGeneratorMap> checksumsMap;
        List<ChecksumGenerator> generators;

        try {
            checksumsMap = checksumExtraction
                .extract()
                .stream()
                .map(ChecksumWithGeneratorMap::createFromChecksum)
                .toList();

            generators = checksumsMap
                .stream()
                .map(ChecksumWithGeneratorMap::getChecksumGenerator)
                .toList();
        } catch (Exception e) {
            exceptionConsumer.accept(e);
            return;
        }



        // Processing
        if (this.isCanceled()) {
            return;
        }

        try {
            generatorUpdate.update(generators, progressConsumer);
        } catch (Exception e) {
            exceptionConsumer.accept(e);
            return;
        }



        // Result collecting
        if (this.isCanceled()) {
            return;
        }

        ChecksumCheckingResult result = new ChecksumCheckingResult();

        checksumsMap
            .stream()
            .map(ChecksumWithGeneratorMap::decodeIntoCheckerChecksum)
            .forEach(result::addChecksum);

        resultConsumer.accept(result);
    }

    public void cancelChecksumChecking() {
        generatorUpdate.cancel();
    }



    public void initSetup() {
        this.exceptionConsumer = _ -> {};
        this.problemConsumer = _ -> {};
        this.progressConsumer = _ -> {};
        this.resultConsumer = _ -> {};

        this.inputProblemDetection = new ProblemDetection() {};
        this.generatorUpdate = new GeneratorUpdate() {};
        this.checksumProblemDetection = new ProblemDetection() {};
        this.checksumExtraction = new ChecksumExtraction() {};

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

    public void setResultConsumer(Consumer<ChecksumCheckingResult> resultConsumer) {
        this.resultConsumer = resultConsumer;
    }

    public void setInputProblemDetection(ProblemDetection inputProblemDetection) {
        this.inputProblemDetection = inputProblemDetection;
    }

    public void setChecksumProblemDetection(ProblemDetection checksumProblemDetection) {
        this.checksumProblemDetection = checksumProblemDetection;
    }

    public void setGeneratorUpdate(GeneratorUpdate generatorUpdate) {
        this.generatorUpdate = generatorUpdate;
    }

    public void setChecksumExtraction(ChecksumExtraction checksumExtraction) {
        this.checksumExtraction = checksumExtraction;
    }



    private boolean isCanceled() {
        return canceled;
    }



    private static class ChecksumWithGeneratorMap {

        private Checksum checksum;
        private ChecksumGenerator generator;



        private ChecksumWithGeneratorMap() {
        }



        public static ChecksumWithGeneratorMap createFromChecksum(Checksum checksum) {
            ChecksumWithGeneratorMap map = new ChecksumWithGeneratorMap();
            map.checksum = checksum;
            map.generator = ChecksumGenerator.createFromChecksum(checksum);

            return map;
        }



        public CheckerChecksum decodeIntoCheckerChecksum() {
            CheckerChecksum checkerChecksum = new CheckerChecksum();
            checkerChecksum.setOfficial(checksum);
            checkerChecksum.setGenerated(generator.decodeIntoChecksum());
            return checkerChecksum;
        }

        public ChecksumGenerator getChecksumGenerator() {
            return generator;
        }
    }
}
