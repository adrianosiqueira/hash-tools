package hashtools.module.checking.service;

import hashtools.core.event.HashToolsEventBus;
import hashtools.module.checking.event.CheckingEndedEvent;
import hashtools.module.checking.event.CheckingRequestedEvent;
import hashtools.module.checking.event.CheckingResultFormattedEvent;
import hashtools.module.checking.facade.CheckingResultFormatting;
import hashtools.module.checking.facade.ChecksumChecking;
import hashtools.module.checking.model.CheckingContext;
import hashtools.module.checking.model.CheckingResult;

import java.util.Objects;

public class CheckingService {

    private final HashToolsEventBus eventBus;



    private CheckingService(HashToolsEventBus eventBus) {
        this.eventBus = eventBus;
    }



    public static void registerListeners(HashToolsEventBus eventBus) {
        Objects.requireNonNull(eventBus, "The event bus cannot be null");

        CheckingService service = new CheckingService(eventBus);

        eventBus.register(CheckingRequestedEvent.class, service::performChecksumChecking);
        eventBus.register(CheckingEndedEvent.class, service::performResultFormatting);
    }



    private void performChecksumChecking(CheckingRequestedEvent event) {
        CheckingContext context = event.getContext();

        ChecksumChecking checksumChecking = new ChecksumChecking();
        CheckingResult result = checksumChecking.perform(context);

        eventBus.publish(new CheckingEndedEvent(result));
    }

    private void performResultFormatting(CheckingEndedEvent event) {
        CheckingResult result = event.getResult();

        CheckingResultFormatting resultFormatting = new CheckingResultFormatting();
        String formatted = resultFormatting.format(result);

        eventBus.publish(new CheckingResultFormattedEvent(formatted));
    }
}
