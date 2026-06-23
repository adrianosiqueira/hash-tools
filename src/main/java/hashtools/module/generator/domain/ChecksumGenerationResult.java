package hashtools.module.generator.domain;

import hashtools.core.checksum.Checksum;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ChecksumGenerationResult {

    private List<Checksum> checksums;
    private String identification;



    public ChecksumGenerationResult() {
        this.checksums = new ArrayList<>();
        this.identification = "";
    }



    public void addChecksum(Checksum checksum) {
        checksums.add(checksum);
    }



    public List<Checksum> getChecksums() {
        return checksums;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(Supplier<String> identification) {
        this.identification = identification.get();
    }
}
