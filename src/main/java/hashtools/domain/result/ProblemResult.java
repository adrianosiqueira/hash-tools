package hashtools.domain.result;

import hashtools.service.ChecksumComparisonService;
import hashtools.service.ChecksumGenerationService;

public final class ProblemResult
    implements
    ChecksumComparisonService.Result,
    ChecksumGenerationService.Result {

    private String description;



    public ProblemResult(String description) {
        this.description = description;
    }



    public String getDescription() {
        return description;
    }
}
