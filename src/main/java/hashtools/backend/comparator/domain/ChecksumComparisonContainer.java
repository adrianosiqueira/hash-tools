package hashtools.backend.comparator.domain;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class ChecksumComparisonContainer {

    private ChecksumComparisonResult result;
    private String problem;
    private Exception exception;



    private ChecksumComparisonContainer(ChecksumComparisonResult result, String problem, Exception exception) {
        this.result = result;
        this.problem = problem;
        this.exception = exception;
    }



    public static ChecksumComparisonContainer result(ChecksumComparisonResult result) {
        return new ChecksumComparisonContainer(result, null, null);
    }

    public static ChecksumComparisonContainer problem(String problem) {
        return new ChecksumComparisonContainer(null, problem, null);
    }

    public static ChecksumComparisonContainer exception(Exception exception) {
        return new ChecksumComparisonContainer(null, null, exception);
    }



    public void consumeResultIfPresent(Consumer<ChecksumComparisonResult> consumer) {
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
