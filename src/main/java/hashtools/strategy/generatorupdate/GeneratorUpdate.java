package hashtools.strategy.generatorupdate;

import hashtools.domain.algorithm.ChecksumGenerator;
import hashtools.domain.commom.Result;

import java.util.Collection;
import java.util.function.Consumer;

public interface GeneratorUpdate {

    @Deprecated(forRemoval = true)
    default void update(Collection<ChecksumGenerator> generators, Consumer<Double> progressConsumer) throws RuntimeException {
    }

    @Deprecated(forRemoval = true)
    default void cancel() {
    }

    default Result<Void, String> updateGenerators(Collection<ChecksumGenerator> generators, Consumer<Double> progressTracker) {
        return Result.ok(null);
    }
}
