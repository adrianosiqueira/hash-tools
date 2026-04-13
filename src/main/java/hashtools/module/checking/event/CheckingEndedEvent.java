package hashtools.module.checking.event;

import hashtools.core.event.HashToolsEvent;
import hashtools.module.checking.model.CheckingResult;

public class CheckingEndedEvent implements HashToolsEvent {

    private CheckingResult result;



    public CheckingEndedEvent(CheckingResult result) {
        this.result = result;
    }



    public CheckingResult getResult() {
        return result;
    }
}
