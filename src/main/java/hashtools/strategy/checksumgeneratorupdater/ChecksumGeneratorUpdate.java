package hashtools.strategy.checksumgeneratorupdater;

import hashtools.domain.algorithm.ChecksumGenerator;

import java.util.Collection;

public interface ChecksumGeneratorUpdate {

    default Result updateChecksumGenerators(Collection<ChecksumGenerator> generators) {
        return new Result.Failure(new IllegalStateException("There is no implementation set"));
    }

    default void cancelChecksumGeneratorsUpdate() {
    }



    sealed interface Result {

        record Failure(Exception exception) implements Result {}

        record Success() implements Result {}
    }
}
