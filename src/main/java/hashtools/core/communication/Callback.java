package hashtools.core.communication;

import java.util.Objects;
import java.util.function.Consumer;

public class Callback<T> {

    private Consumer<T> resultConsumer;
    private Consumer<Double> progressConsumer;

    private Consumer<String> problemConsumer;
    private Consumer<Exception> exceptionConsumer;



    public Callback() {
        this.resultConsumer = _ -> {};
        this.progressConsumer = _ -> {};
        this.problemConsumer = _ -> {};
        this.exceptionConsumer = _ -> {};
    }



    public void sendResult(T result) {
        resultConsumer.accept(result);
    }

    public void sendProgress(double progress) {
        progressConsumer.accept(progress);
    }

    public void sendProblem(String problem) {
        problemConsumer.accept(problem);
    }

    public void sendException(Exception exception) {
        exceptionConsumer.accept(exception);
    }



    public void addResultConsumer(Consumer<T> consumer) {
        Objects.requireNonNull(consumer);

        this.resultConsumer = this
            .resultConsumer
            .andThen(consumer);
    }

    public void addProgressConsumer(Consumer<Double> consumer) {
        Objects.requireNonNull(consumer);

        this.progressConsumer = this
            .progressConsumer
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
