package hashtools.backend.checker.domain;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class ChecksumCheckingContainer {

    private ChecksumCheckingResult result;
    private String problem;
    private Exception exception;



    private ChecksumCheckingContainer(ChecksumCheckingResult result, String problem, Exception exception) {
        this.result = result;
        this.problem = problem;
        this.exception = exception;
    }



    public static ChecksumCheckingContainer result(ChecksumCheckingResult result) {
        return new ChecksumCheckingContainer(result, null, null);
    }

    public static ChecksumCheckingContainer problem(String problem) {
        return new ChecksumCheckingContainer(null, problem, null);
    }

    public static ChecksumCheckingContainer exception(Exception exception) {
        return new ChecksumCheckingContainer(null, null, exception);
    }



    public void consumeResultIfPresent(Consumer<ChecksumCheckingResult> consumer) {
        Objects.requireNonNull(consumer);

        Optional
            .ofNullable(result)
            .ifPresent(consumer);
    }

    public void consumeProblemIfPresent(Consumer<String> consumer) {
        Objects.requireNonNull(consumer);

        Optional
            .ofNullable(problem)
            .ifPresent(consumer);
    }

    public void consumeExceptionIfPresent(Consumer<Exception> consumer) {
        Objects.requireNonNull(consumer);

        Optional
            .ofNullable(exception)
            .ifPresent(consumer);
    }
}
