package hashtools.strategy.checksumsource;

import hashtools.domain.checksum.Checksum;
import hashtools.domain.result.CanceledResult;
import hashtools.domain.result.ExceptionResult;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ChecksumSource {

    default Result extractOfficialChecksums() {
        return new SuccessResult(List.of());
    }

    default void cancelChecksumsExtraction() {
    }

    default Optional<String> detectProblem() {
        return Optional.empty();
    }



    sealed interface Result permits CanceledResult, ExceptionResult, SuccessResult {}



    final class SuccessResult implements Result {

        private Collection<Checksum> checksums;



        public SuccessResult(Collection<Checksum> checksums) {
            this.checksums = checksums;
        }



        public Stream<Checksum> getChecksumsStream() {
            return checksums.stream();
        }
    }
}
