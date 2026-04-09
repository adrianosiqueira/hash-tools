package hashtools.module.checking.service;

import hashtools.core.event.HashToolsEventBus;
import hashtools.core.model.Checksum;
import hashtools.core.threadpool.ThreadPoolFactory;
import hashtools.module.checking.event.ChecksumCheckingEndedEvent;
import hashtools.module.checking.event.ChecksumCheckingFormattedEvent;
import hashtools.module.checking.event.ChecksumCheckingRequestedEvent;
import hashtools.module.checking.model.CheckingChecksum;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ChecksumCheckingService {

    private final HashToolsEventBus eventBus;



    public ChecksumCheckingService(HashToolsEventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(ChecksumCheckingRequestedEvent.class, this::performChecksumChecking);
        eventBus.register(ChecksumCheckingEndedEvent.class, this::performResultFormatting);
    }



    public void performChecksumChecking(ChecksumCheckingRequestedEvent event) {
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

            for (Future<CheckingChecksum> future : futureChecksums) {
                CheckingChecksum checksum = future.get();
                endedEvent.addChecksum(checksum);
            }

            eventBus.publish(endedEvent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void performResultFormatting(ChecksumCheckingEndedEvent event) {
        // TODO Finish implementing the formatting logic

        ChecksumCheckingFormattedEvent formattedEvent = new ChecksumCheckingFormattedEvent();
        formattedEvent.setFormattedContent(null);

        eventBus.publish(formattedEvent);
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
