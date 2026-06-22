package hashtools.module.comparator.domain;

import java.util.Objects;
import java.util.function.Consumer;

public class ChecksumComparisonCallback {

    private Consumer<Double> progressConsumer;
    private Consumer<ChecksumComparisonResult> resultConsumer;

    private Consumer<String> problemConsumer;
    private Consumer<Exception> exceptionConsumer;



    public ChecksumComparisonCallback() {
        this.progressConsumer = _ -> {};
        this.resultConsumer = _ -> {};
        this.problemConsumer = _ -> {};
        this.exceptionConsumer = _ -> {};
    }



    public void sendProgress(double progress) {
        progressConsumer.accept(progress);
    }

    public void sendResult(ChecksumComparisonResult result) {
        resultConsumer.accept(result);
    }

    public void sendProblem(String problem) {
        problemConsumer.accept(problem);
    }

    public void sendException(Exception exception) {
        exceptionConsumer.accept(exception);
    }



    public void addProgressConsumer(Consumer<Double> consumer) {
        Objects.requireNonNull(consumer);

        this.progressConsumer = this
            .progressConsumer
            .andThen(consumer);
    }

    public void addResultConsumer(Consumer<ChecksumComparisonResult> consumer) {
        Objects.requireNonNull(consumer);

        this.resultConsumer = this
            .resultConsumer
            .andThen(consumer);
    }

    public void addProblemConsumer(Consumer<String> consumer) {
        Objects.requireNonNull(consumer);

        this.problemConsumer = this
            .problemConsumer
            .andThen(consumer);
    }

    public void addExceptionConsumer(Consumer<Exception> consumer) {
        Objects.requireNonNull(consumer);

        this.exceptionConsumer = this
            .exceptionConsumer
            .andThen(consumer);
    }
}
