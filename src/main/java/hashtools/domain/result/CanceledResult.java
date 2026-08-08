package hashtools.domain.result;

import hashtools.service.ChecksumCheckingService;
import hashtools.service.ChecksumComparisonService;
import hashtools.service.ChecksumGenerationService;
import hashtools.strategy.checksumsource.ChecksumSource;
import hashtools.strategy.inputsource.InputSource;

public final class CanceledResult
    implements
    ChecksumCheckingService.Result,
    ChecksumComparisonService.Result,
    ChecksumGenerationService.Result,
    ChecksumSource.Result,
    InputSource.Result {
}
