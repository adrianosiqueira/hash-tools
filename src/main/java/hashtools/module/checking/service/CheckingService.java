package hashtools.module.checking.service;

import hashtools.core.event.ExceptionThrownEvent;
import hashtools.core.event.HashToolsEventBus;
import hashtools.core.event.HashToolsEventListener;
import hashtools.module.checking.event.CheckingEndedEvent;
import hashtools.module.checking.event.CheckingRequestedEvent;
import hashtools.module.checking.event.CheckingResultFormattedEvent;
import hashtools.module.checking.facade.CheckingResultFormatting;
import hashtools.module.checking.facade.ChecksumChecking;
import hashtools.module.checking.model.CheckingContext;
import hashtools.module.checking.model.CheckingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.Objects;

public class CheckingService implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckingService.class);



    private HashToolsEventBus eventBus;

    private HashToolsEventListener<CheckingRequestedEvent> checkingRequestedListener;
    private HashToolsEventListener<CheckingEndedEvent> checkingEndedListener;



    public CheckingService(HashToolsEventBus eventBus) {
        this.eventBus = Objects.requireNonNull(
            eventBus,
            "The event bus cannot be null"
        );

        this.checkingRequestedListener = this::performChecksumChecking;
        this.checkingEndedListener = this::performResultFormatting;

        this.registerListeners();
    }



    @Override
    public void close() {
        eventBus.unregister(CheckingRequestedEvent.class, checkingRequestedListener);
        eventBus.unregister(CheckingEndedEvent.class, checkingEndedListener);
    }

    private void registerListeners() {
        eventBus.register(CheckingRequestedEvent.class, checkingRequestedListener);
        eventBus.register(CheckingEndedEvent.class, checkingEndedListener);
    }



    private void performChecksumChecking(CheckingRequestedEvent event) {
        try {
            LOGGER.info("Starting to perform the checksum checking.");
            CheckingContext context = event.getContext();

            ChecksumChecking checksumChecking = new ChecksumChecking();
            CheckingResult result = checksumChecking.perform(context);

            eventBus.publish(new CheckingEndedEvent(result));
            LOGGER.info("The checksum checking is finished.");
        } catch (Exception e) {
            LOGGER.error("Failed to perform the checksum checking.", e);
            eventBus.publish(new ExceptionThrownEvent(e));
        }
    }

    private void performResultFormatting(CheckingEndedEvent event) {
        LOGGER.info("Starting to perform the result formatting.");
        CheckingResult result = event.getResult();

        CheckingResultFormatting resultFormatting = new CheckingResultFormatting();
        String formatted = resultFormatting.format(result);

        eventBus.publish(new CheckingResultFormattedEvent(formatted));
        LOGGER.info("The result formatting is finished.");
    }
}
