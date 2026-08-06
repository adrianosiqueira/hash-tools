package hashtools.domain.result;

import hashtools.service.ChecksumCheckingService;
import hashtools.service.ChecksumComparisonService;
import hashtools.service.ChecksumGenerationService;
import hashtools.strategy.checksumgeneratorupdater.ChecksumGeneratorUpdate;
import hashtools.strategy.checksumsource.ChecksumSource;
import hashtools.strategy.inputsource.InputSource;

public final class CanceledResult
    implements
    ChecksumCheckingService.Result,
    ChecksumComparisonService.Result,
    ChecksumGenerationService.Result,
    ChecksumGeneratorUpdate.Result,
    ChecksumSource.Result,
    InputSource.Result {
}
