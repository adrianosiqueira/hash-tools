package hashtools.module.checking.facade;

import hashtools.core.model.Checksum;
import hashtools.core.threadpool.ThreadPoolFactory;
import hashtools.module.checking.event.ChecksumCheckingEndedEvent;
import hashtools.module.checking.event.ChecksumCheckingRequestedEvent;
import hashtools.module.checking.model.CheckingChecksum;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ChecksumChecking {

    public ChecksumCheckingEndedEvent perform(ChecksumCheckingRequestedEvent event) {
        List<Future<CheckingChecksum>> futureChecksums = new ArrayList<>();
        List<Checksum> officialChecksums = event.extractOfficialChecksums();

        try (ExecutorService executor = ThreadPoolFactory.createDaemonPool()) {
            for (Checksum official : officialChecksums) {
                futureChecksums.add(executor.submit(
                    () -> this.generateChecksumMappingToDTO(official, event)
                ));
            }
        }

        try {
            ChecksumCheckingEndedEvent endedEvent = new ChecksumCheckingEndedEvent();
            endedEvent.setIdentifier(event::getIdentification);

            for (Future<CheckingChecksum> future : futureChecksums) {
                CheckingChecksum checksum = future.get();
                endedEvent.addChecksum(checksum);
            }

            return endedEvent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    private CheckingChecksum generateChecksumMappingToDTO(Checksum official, ChecksumCheckingRequestedEvent event) {
        Checksum generated = event.generateChecksum(official.getAlgorithm());

        CheckingChecksum checksum = new CheckingChecksum();
        checksum.setOfficialChecksum(official);
        checksum.setGeneratedChecksum(generated);
        checksum.setIdentifier(event::getIdentification);

        return checksum;
    }
}
