package hashtools.module.comparison;

import hashtools.core.model.Checksum;
import hashtools.core.strategy.checksumidentifier.ChecksumIdentifier;
import hashtools.core.threadpool.ThreadPoolFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ChecksumComparison {

    public ChecksumComparisonResult perform(ChecksumComparisonContext context) throws Exception {
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



        ChecksumComparisonResult result = new ChecksumComparisonResult();
        result.setChecksum1(futureChecksum1.get());
        result.setChecksum2(futureChecksum2.get());

        return result;
    }



    private ChecksumComparisonDTO generateChecksumMappingToDTO(Callable<Checksum> generator, ChecksumIdentifier identifier) throws Exception {
        ChecksumComparisonDTO checksum = new ChecksumComparisonDTO();
        checksum.setChecksum(generator.call());
        checksum.setIdentifier(identifier);

        return checksum;
    }
}
