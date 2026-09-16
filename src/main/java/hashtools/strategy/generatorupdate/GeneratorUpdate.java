package hashtools.strategy.generatorupdate;

import hashtools.domain.checksum.ChecksumGenerator;
import hashtools.domain.commom.Result;

import java.util.Collection;
import java.util.function.Consumer;

public interface GeneratorUpdate {

    default Result<Void, String> updateGenerators(Collection<ChecksumGenerator> generators, Consumer<Double> progressTracker) {
        return new Result.Ok<>(null);
    }
}
