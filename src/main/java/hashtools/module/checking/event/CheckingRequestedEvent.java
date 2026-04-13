package hashtools.module.checking.event;

import hashtools.core.event.HashToolsEvent;
import hashtools.module.checking.model.CheckingContext;

public class CheckingRequestedEvent implements HashToolsEvent {

    private CheckingContext context;



    public CheckingRequestedEvent(CheckingContext context) {
        this.context = context;
    }



    public CheckingContext getContext() {
        return context;
    }
}
