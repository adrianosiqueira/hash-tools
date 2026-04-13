package hashtools.module.checking.event;

import hashtools.core.event.HashToolsEvent;

public class CheckingResultFormattedEvent implements HashToolsEvent {

    private String content;



    public CheckingResultFormattedEvent(String content) {
        this.content = content;
    }



    public String getContent() {
        return content;
    }
}
