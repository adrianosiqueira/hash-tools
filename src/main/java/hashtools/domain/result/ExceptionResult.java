package hashtools.domain.result;

import hashtools.service.ChecksumCheckingService;
import hashtools.service.ChecksumComparisonService;
import hashtools.service.ChecksumGenerationService;

public record ExceptionResult(
    Exception exception
) implements ChecksumCheckingService.Result, ChecksumComparisonService.Result, ChecksumGenerationService.Result {

    @SuppressWarnings("CallToPrintStackTrace")
    public void printStackTrace() {
        exception.printStackTrace();
    }
}
