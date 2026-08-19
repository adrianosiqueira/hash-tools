package hashtools.domain.result;

import hashtools.service.ChecksumGenerationService;

import java.util.Objects;
import java.util.function.Consumer;

public final class ExceptionResult implements ChecksumGenerationService.Result {

    private Exception exception;



    public ExceptionResult(Exception exception) {
        this.exception = exception;
    }



    public void consumeException(Consumer<Exception> consumer) {
        Objects
            .requireNonNull(consumer)
            .accept(exception);
    }

    public void throwAsRuntimeException() throws RuntimeException {
        throw new RuntimeException(exception);
    }
}
