package hashtools.module.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChecksumGenerationResult {

    private List<ChecksumGenerationDTO> checksums;



    public ChecksumGenerationResult() {
        this.checksums = new ArrayList<>();
    }



    public void addChecksum(ChecksumGenerationDTO checksum) {
        ChecksumGenerationDTO c = Optional
            .ofNullable(checksum)
            .orElseGet(ChecksumGenerationDTO::new);

        checksums.add(c);
    }

    public List<ChecksumGenerationDTO> getChecksums() {
        return checksums;
    }
}
