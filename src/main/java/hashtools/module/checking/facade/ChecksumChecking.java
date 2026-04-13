package hashtools.module.checking.facade;

import hashtools.core.model.Checksum;
import hashtools.core.threadpool.ThreadPoolFactory;
import hashtools.module.checking.model.CheckingChecksum;
import hashtools.module.checking.model.CheckingContext;
import hashtools.module.checking.model.CheckingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ChecksumChecking {

    public CheckingResult perform(CheckingContext context) {
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

            return result;
        } catch (Exception e) {
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
