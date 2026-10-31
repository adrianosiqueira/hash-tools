package hashtools.generator.domain;

import hashtools.core.domain.Algorithm;
import hashtools.core.domain.Checksum;
import hashtools.core.domain.ChecksumGenerator;
import hashtools.core.strategy.ChecksumGeneratorUpdate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class GenerationChecksumGenerators {

    private Collection<ChecksumGenerator> generators;



    public GenerationChecksumGenerators() {
        this.generators = new ArrayList<>();
    }



    public void addFromAlgorithms(Collection<Algorithm> algorithms) {
        for (var algorithm : algorithms) {
            var generator = algorithm.createChecksumGenerator();
            generators.add(generator);
        }
    }

    public void update(ChecksumGeneratorUpdate update)
    throws IOException, InterruptedException {
        update.update(generators);
    }

    public void decodeAndCollect(Consumer<Checksum> consumer) {
        for (var generator : generators) {
            var checksum = generator.decodeIntoChecksum();
            consumer.accept(checksum);
        }
    }
}
