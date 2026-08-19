package hashtools.domain.result;

import hashtools.service.ChecksumCheckingService;
import hashtools.service.ChecksumComparisonService;
import hashtools.service.ChecksumGenerationService;

public final class CanceledResult
    implements
    ChecksumCheckingService.Result,
    ChecksumComparisonService.Result,
    ChecksumGenerationService.Result {
}
