package hashtools.module.checking;

import hashtools.core.model.Checksum;
import hashtools.core.threadpool.ThreadPoolFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ChecksumChecking {

    public ChecksumCheckingResult perform(ChecksumCheckingContext context) {
        List<Future<ChecksumCheckingDTO>> futureChecksums = new ArrayList<>();
        List<Checksum> officialChecksums = context.extractOfficialChecksums();

        try (ExecutorService executor = ThreadPoolFactory.createDaemonPool()) {
            for (Checksum official : officialChecksums) {
                futureChecksums.add(executor.submit(
                    () -> this.generateChecksumMappingToDTO(official, context)
                ));
            }
        }

        try {
            ChecksumCheckingResult result = new ChecksumCheckingResult();

            for (Future<ChecksumCheckingDTO> future : futureChecksums) {
                ChecksumCheckingDTO checksum = future.get();
                result.addChecksum(checksum);
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    private ChecksumCheckingDTO generateChecksumMappingToDTO(Checksum official, ChecksumCheckingContext context) {
        Checksum generated = context.generateChecksum(official.getAlgorithm());

        ChecksumCheckingDTO checksum = new ChecksumCheckingDTO();
        checksum.setOfficialChecksum(official);
        checksum.setGeneratedChecksum(generated);
        checksum.setIdentifier(context::getIdentification);

        return checksum;
    }
}
