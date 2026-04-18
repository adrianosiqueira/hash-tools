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

import java.io.Closeable;
import java.util.Objects;

public class CheckingService implements Closeable {

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

        IO.println(this.getClass().getSimpleName() + " unregistered listeners");
    }

    private void registerListeners() {
        eventBus.register(CheckingRequestedEvent.class, checkingRequestedListener);
        eventBus.register(CheckingEndedEvent.class, checkingEndedListener);

        IO.println(this.getClass().getSimpleName() + " registered listeners");
    }



    private void performChecksumChecking(CheckingRequestedEvent event) {
        try {
            CheckingContext context = event.getContext();

            ChecksumChecking checksumChecking = new ChecksumChecking();
            CheckingResult result = checksumChecking.perform(context);

            eventBus.publish(new CheckingEndedEvent(result));
        } catch (Exception e) {
            eventBus.publish(new ExceptionThrownEvent(e));
        }
    }

    private void performResultFormatting(CheckingEndedEvent event) {
        CheckingResult result = event.getResult();

        CheckingResultFormatting resultFormatting = new CheckingResultFormatting();
        String formatted = resultFormatting.format(result);

        eventBus.publish(new CheckingResultFormattedEvent(formatted));
    }
}
