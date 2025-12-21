package hash_tools.backend.result;

import hash_tools.backend.checksum.Checksum;

import java.util.List;
import java.util.function.Consumer;

public class GeneratorResult {

    private List<Checksum> checksums;
    private String identification;



    public GeneratorResult(List<Checksum> checksums, String identification) {
        this.checksums = checksums;
        this.identification = identification;
    }



    public GeneratorResult consume(Consumer<GeneratorResult> consumer) {
        consumer.accept(this);
        return this;
    }



    public List<Checksum> checksums() {
        return checksums;
    }

    public String identification() {
        return identification;
    }
}
