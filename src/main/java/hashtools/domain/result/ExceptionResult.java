package hashtools.domain.result;

import hashtools.service.ChecksumCheckingService;
import hashtools.service.ChecksumComparisonService;
import hashtools.service.ChecksumGenerationService;

import java.util.Objects;
import java.util.function.Consumer;

public final class ExceptionResult implements ChecksumCheckingService.Result, ChecksumComparisonService.Result, ChecksumGenerationService.Result {

    private Exception exception;



    public ExceptionResult(Exception exception) {
        this.exception = exception;
    }



    public void consumeException(Consumer<Exception> consumer) {
        Objects
            .requireNonNull(consumer)
            .accept(exception);
    }
}
