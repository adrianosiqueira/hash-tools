package hashtools.module.generation;

import java.util.ArrayList;
import java.util.List;

public class ChecksumGenerationResult {

    private List<ChecksumGenerationDTO> checksums;



    public ChecksumGenerationResult() {
        this.checksums = new ArrayList<>();
    }



    public void addChecksum(ChecksumGenerationDTO checksum) {
        checksums.add(checksum);
    }

    public List<ChecksumGenerationDTO> getChecksums() {
        return checksums;
    }
}
