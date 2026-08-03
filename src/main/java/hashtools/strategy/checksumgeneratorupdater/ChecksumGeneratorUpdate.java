package hashtools.strategy.checksumgeneratorupdater;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.domain.result.CanceledResult;
import hashtools.domain.result.ExceptionResult;

import java.util.Collection;

public interface ChecksumGeneratorUpdate {

    default Result updateChecksumGenerators(Collection<ChecksumGenerator> generators) {
        return new ExceptionResult(new IllegalStateException("There is no implementation set"));
    }

    default void cancelChecksumGeneratorsUpdate() {
    }



    sealed interface Result permits CanceledResult, ExceptionResult, SuccessResult {
    }

    final class SuccessResult implements Result {}
}
