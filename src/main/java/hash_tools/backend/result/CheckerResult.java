package hash_tools.backend.result;

import hash_tools.backend.checksum.CheckingChecksum;

import java.util.List;
import java.util.function.Consumer;

public class CheckerResult {

    private List<CheckingChecksum> checksums;
    private double matchingRatio;



    public CheckerResult(List<CheckingChecksum> checksums, double matchingRatio) {
        this.checksums = checksums;
        this.matchingRatio = matchingRatio;
    }



    public CheckerResult consume(Consumer<CheckerResult> consumer) {
        consumer.accept(this);
        return this;
    }



    public List<CheckingChecksum> checksums() {
        return checksums;
    }

    public double matchingRatio() {
        return matchingRatio;
    }
}
