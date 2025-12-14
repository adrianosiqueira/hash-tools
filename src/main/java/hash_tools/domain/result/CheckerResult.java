package hash_tools.domain.result;

import hash_tools.domain.checksum.CheckingChecksum;

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
