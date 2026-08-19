package hashtools.domain.result;

import hashtools.domain.checksum.Checksum;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class ChecksumGenerationResult {

    private List<Checksum> checksums;
    private String identification;



    public ChecksumGenerationResult() {
        this.checksums = new ArrayList<>();
        this.identification = "";
    }



    public void addChecksum(Checksum checksum) {
        checksums.add(checksum);
    }

    public String formatForSaving() {
        return checksums
            .stream()
            .map(checksum -> checksum.getHash() + "  " + identification)
            .collect(Collectors.joining("\n"));
    }

    public void setIdentification(Supplier<String> identification) {
        this.identification = identification.get();
    }
}
