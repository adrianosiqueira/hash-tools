package hashtools.strategy.checksumextraction;

import hashtools.domain.checksum.Checksum;

import java.util.Collection;

public interface ChecksumExtraction {

    Result extractOfficialChecksums();



    sealed interface Result {

        record Failure(Exception exception) implements Result {}

        record Success(Collection<Checksum> checksums) implements Result {}
    }
}
