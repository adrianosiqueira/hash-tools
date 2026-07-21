package hashtools.strategy.checksumgeneratorupdater;

import hashtools.domain.algorithm.ChecksumGenerator;

import java.util.Collection;

public interface ChecksumGeneratorUpdater {

    Result updateChecksumGenerators(Collection<ChecksumGenerator> generators);

    void cancelChecksumGeneratorsUpdate();



    sealed interface Result {

        record Failure(Exception exception) implements Result {}

        record Success() implements Result {}
    }
}
