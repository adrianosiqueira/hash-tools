package hashtools;

import hashtools.core.event.EventService;
import hashtools.core.event.HashToolsEventBus;
import hashtools.view.ApplicationWindow;
import javafx.application.Application;

public class Main {

    private static final HashToolsEventBus EVENT_BUS = EventService.INSTANCE;



    static void main() {
        Application.launch(ApplicationWindow.class);
    }



    public static HashToolsEventBus getEventBus() {
        return EVENT_BUS;
    }
}
