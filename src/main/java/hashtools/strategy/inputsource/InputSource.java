package hashtools.strategy.inputsource;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.domain.result.CanceledResult;
import hashtools.domain.result.ExceptionResult;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

public interface InputSource {

    default Result updateChecksumGenerators(Collection<ChecksumGenerator> generators) {
        return new SuccessResult();
    }

    default void cancelChecksumGeneratorsUpdate() {
    }



    @Deprecated(forRemoval = true)
    default void updateMessageDigest(Collection<MessageDigest> messageDigests) throws IOException {
    }

    default String getIdentification() {
        return "";
    }

    default Optional<String> detectProblem() {
        return Optional.empty();
    }

    @Deprecated(forRemoval = true)
    default void cancel() {
    }

    default void setProgressTracking(Consumer<Double> tracking) {
    }



    sealed interface Result permits CanceledResult, ExceptionResult, SuccessResult {}



    final class SuccessResult implements Result {}
}
