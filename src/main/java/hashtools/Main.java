package hashtools;

import hashtools.module.checking.ChecksumChecking;
import hashtools.module.checking.ChecksumCheckingContext;
import hashtools.module.comparison.ChecksumComparison;
import hashtools.module.comparison.ChecksumComparisonContext;
import hashtools.module.generation.ChecksumGeneration;
import hashtools.module.generation.ChecksumGenerationContext;
import hashtools.service.EventService;
import hashtools.view.ApplicationWindow;
import javafx.application.Application;

public class Main {

    static void main() {
        new Thread(() -> {
            // Open UI earlier because it is slow
            Application.launch(ApplicationWindow.class);
        }).start();



        ChecksumChecking checksumChecking = new ChecksumChecking();
        ChecksumComparison checksumComparison = new ChecksumComparison();
        ChecksumGeneration checksumGeneration = new ChecksumGeneration();

        EventService eventService = EventService.INSTANCE;
        eventService.register(ChecksumCheckingContext.class, checksumChecking::perform);
        eventService.register(ChecksumComparisonContext.class, checksumComparison::perform);
        eventService.register(ChecksumGenerationContext.class, checksumGeneration::perform);
    }
}
