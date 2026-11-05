package hashtools.checker.domain;

import hashtools.core.domain.Checksum;
import hashtools.core.domain.ChecksumGenerator;
import hashtools.core.strategy.ChecksumGeneratorUpdate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CheckingChecksumGenerators {

    private Map<Checksum, ChecksumGenerator> generatorMap;



    public CheckingChecksumGenerators() {
        this.generatorMap = new HashMap<>();
    }



    public void addFromOfficialChecksums(OfficialChecksums checksums) {
        for (var checksum : checksums.getChecksums()) {
            var algorithm = checksum.getAlgorithm();
            var generator = algorithm.createChecksumGenerator();

            generatorMap.put(checksum, generator);
        }
    }

    public void update(ChecksumGeneratorUpdate update)
    throws IOException, InterruptedException {
        var generators = generatorMap.values();
        update.update(generators);
    }

    public void decodeAndCollect(Consumer<CheckingChecksum> consumer) {
        for (var entry : generatorMap.entrySet()) {
            var generator = entry.getValue();

            var officialChecksum = entry.getKey();
            var generatedChecksum = generator.decodeIntoChecksum();

            var checkingChecksum = new CheckingChecksum();
            checkingChecksum.setOfficialChecksum(officialChecksum);
            checkingChecksum.setGeneratedChecksum(generatedChecksum);

            consumer.accept(checkingChecksum);
        }
    }
}
