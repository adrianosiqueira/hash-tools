package hashtools.domain.result;

import hashtools.service.ChecksumCheckingService;
import hashtools.service.ChecksumComparisonService;
import hashtools.service.ChecksumGenerationService;

public record ProblemResult(
    String description
) implements ChecksumCheckingService.Result, ChecksumComparisonService.Result, ChecksumGenerationService.Result {
}
