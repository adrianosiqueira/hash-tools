package hashtools.backend.generator.domain;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class ChecksumGenerationContainer {

    private ChecksumGenerationResult result;
    private String problem;
    private Exception exception;



    private ChecksumGenerationContainer(ChecksumGenerationResult result, String problem, Exception exception) {
        this.result = result;
        this.problem = problem;
        this.exception = exception;
    }



    public static ChecksumGenerationContainer result(ChecksumGenerationResult result) {
        return new ChecksumGenerationContainer(result, null, null);
    }

    public static ChecksumGenerationContainer problem(String problem) {
        return new ChecksumGenerationContainer(null, problem, null);
    }

    public static ChecksumGenerationContainer exception(Exception exception) {
        return new ChecksumGenerationContainer(null, null, exception);
    }



    public void consumeResultIfPresent(Consumer<ChecksumGenerationResult> consumer) {
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
