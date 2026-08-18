package hashtools.strategy.generatorupdate;

import hashtools.domain.algorithm.ChecksumGenerator;

import java.util.Collection;
import java.util.function.Consumer;

public interface GeneratorUpdate {

    default void update(Collection<ChecksumGenerator> generators, Consumer<Double> progressConsumer) throws RuntimeException {
    }

    default void cancel() {
    }
}
