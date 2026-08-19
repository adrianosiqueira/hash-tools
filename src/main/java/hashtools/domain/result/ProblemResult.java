package hashtools.domain.result;

import hashtools.service.ChecksumGenerationService;

public final class ProblemResult implements ChecksumGenerationService.Result {

    private String description;



    public ProblemResult(String description) {
        this.description = description;
    }



    public String getDescription() {
        return description;
    }
}
