package hashtools.module.checking.service;

import hashtools.core.event.HashToolsEventBus;
import hashtools.module.checking.event.CheckingEndedEvent;
import hashtools.module.checking.event.CheckingRequestedEvent;
import hashtools.module.checking.event.CheckingResultFormattedEvent;
import hashtools.module.checking.facade.ChecksumChecking;
import hashtools.module.checking.facade.ChecksumCheckingEndedEventFormatting;
import hashtools.module.checking.model.CheckingContext;
import hashtools.module.checking.model.CheckingResult;

public class ChecksumCheckingService {

    private final HashToolsEventBus eventBus;



    public ChecksumCheckingService(HashToolsEventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(CheckingRequestedEvent.class, this::performChecksumChecking);
        eventBus.register(CheckingEndedEvent.class, this::performResultFormatting);
    }



    private void performChecksumChecking(CheckingRequestedEvent event) {
        CheckingContext context = event.getContext();

        ChecksumChecking checksumChecking = new ChecksumChecking();
        CheckingResult result = checksumChecking.perform(context);

        eventBus.publish(new CheckingEndedEvent(result));
    }

    private void performResultFormatting(CheckingEndedEvent event) {
        CheckingResult result = event.getResult();

        ChecksumCheckingEndedEventFormatting resultFormatting = new ChecksumCheckingEndedEventFormatting();
        String formatted = resultFormatting.format(result);

        eventBus.publish(new CheckingResultFormattedEvent(formatted));
    }
}
