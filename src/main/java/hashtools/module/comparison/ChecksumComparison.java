package hashtools.module.comparison;

import hashtools.core.model.Checksum;
import hashtools.core.strategy.checksumidentifier.ChecksumIdentifier;
import hashtools.core.threadpool.ThreadPoolFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Supplier;

public class ChecksumComparison {

    public ChecksumComparisonResult perform(ChecksumComparisonContext context) {
        Future<ChecksumComparisonDTO> futureChecksum1;
        Future<ChecksumComparisonDTO> futureChecksum2;

        try (ExecutorService executor = ThreadPoolFactory.createDaemonPool()) {
            futureChecksum1 = executor.submit(() -> this.generateChecksumMappingToDTO(
                context::generateChecksum1,
                context::getIdentification1
            ));

            futureChecksum2 = executor.submit(() -> this.generateChecksumMappingToDTO(
                context::generateChecksum2,
                context::getIdentification2
            ));
        }

        try {
            ChecksumComparisonResult result = new ChecksumComparisonResult();
            result.setChecksum1(futureChecksum1.get());
            result.setChecksum2(futureChecksum2.get());

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    private ChecksumComparisonDTO generateChecksumMappingToDTO(Supplier<Checksum> generator, ChecksumIdentifier identifier) {
        ChecksumComparisonDTO checksum = new ChecksumComparisonDTO();
        checksum.setChecksum(generator.get());
        checksum.setIdentifier(identifier);

        return checksum;
    }
}
