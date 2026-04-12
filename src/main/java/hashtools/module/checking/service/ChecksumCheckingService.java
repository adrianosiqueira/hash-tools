package hashtools.module.checking.service;

import hashtools.core.event.HashToolsEventBus;
import hashtools.module.checking.event.ChecksumCheckingEndedEvent;
import hashtools.module.checking.event.ChecksumCheckingFormattedEvent;
import hashtools.module.checking.event.ChecksumCheckingRequestedEvent;
import hashtools.module.checking.facade.ChecksumChecking;
import hashtools.module.checking.facade.ChecksumCheckingEndedEventFormatting;

public class ChecksumCheckingService {

    private final HashToolsEventBus eventBus;



    public ChecksumCheckingService(HashToolsEventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(ChecksumCheckingRequestedEvent.class, this::performChecksumChecking);
        eventBus.register(ChecksumCheckingEndedEvent.class, this::performResultFormatting);
    }



    public void performChecksumChecking(ChecksumCheckingRequestedEvent event) {
        ChecksumChecking checksumChecking = new ChecksumChecking();
        ChecksumCheckingEndedEvent endedEvent = checksumChecking.perform(event);

        eventBus.publish(endedEvent);
    }

    public void performResultFormatting(ChecksumCheckingEndedEvent event) {
        ChecksumCheckingEndedEventFormatting eventFormatting = new ChecksumCheckingEndedEventFormatting();
        ChecksumCheckingFormattedEvent formattedEvent = eventFormatting.format(event);

        eventBus.publish(formattedEvent);
    }
}
