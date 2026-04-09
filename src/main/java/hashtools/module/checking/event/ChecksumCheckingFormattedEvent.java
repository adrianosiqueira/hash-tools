package hashtools.module.checking.event;

import hashtools.core.event.HashToolsEvent;

import java.util.Optional;
import java.util.function.Consumer;

public class ChecksumCheckingFormattedEvent implements HashToolsEvent {

    private String formattedContent;



    public ChecksumCheckingFormattedEvent() {
        this.setFormattedContent("");
    }



    public void setFormattedContent(String formattedContent) {
        this.formattedContent = Optional
            .ofNullable(formattedContent)
            .orElse("");
    }

    public void consumeContent(Consumer<String> consumer) {
        consumer.accept(formattedContent);
    }
}
