package hashtools.strategy.checksumextraction;

import hashtools.domain.checksum.Checksum;

import java.util.Collection;

public interface ChecksumExtraction {

    default Result extractOfficialChecksums() {
        return new Result.Failure(new IllegalStateException("There is no implementation set"));
    }



    sealed interface Result {

        record Failure(Exception exception) implements Result {}

        record Success(Collection<Checksum> checksums) implements Result {}
    }
}
