package hashtools.module.checking.facade;

import hashtools.core.model.Checksum;
import hashtools.core.threadpool.ThreadPoolFactory;
import hashtools.module.checking.model.CheckingChecksum;
import hashtools.module.checking.model.CheckingContext;
import hashtools.module.checking.model.CheckingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ChecksumChecking {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChecksumChecking.class);



    public CheckingResult perform(CheckingContext context) {
        LOGGER.info("Starting to perform the checksum checking.");

        List<Future<CheckingChecksum>> futureChecksums = new ArrayList<>();
        List<Checksum> officialChecksums = context.extractOfficialChecksums();

        try (ExecutorService executor = ThreadPoolFactory.createDaemonPool()) {
            for (Checksum official : officialChecksums) {
                futureChecksums.add(executor.submit(
                    () -> this.generateChecksum(official, context)
                ));
            }
        }

        try {
            CheckingResult result = new CheckingResult();
            result.setIdentifier(context::getIdentification);

            for (Future<CheckingChecksum> future : futureChecksums) {
                CheckingChecksum checksum = future.get();
                result.addChecksum(checksum);
            }

            LOGGER.info("The checksum checking is finished.");
            return result;
        } catch (Exception e) {
            LOGGER.error("Failed to perform the checksum checking.", e);
            throw new RuntimeException(e);
        }
    }



    private CheckingChecksum generateChecksum(Checksum official, CheckingContext context) {
        Checksum generated = context.generateChecksum(official.getAlgorithm());

        CheckingChecksum checksum = new CheckingChecksum();
        checksum.setOfficialChecksum(official);
        checksum.setGeneratedChecksum(generated);

        return checksum;
    }
}
