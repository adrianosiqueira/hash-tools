package hash_tools.frontend.javafx;

import javafx.application.Platform;

public class AsyncRunner {

    public void runAsync(Runnable runnable) {
        Thread
            .ofPlatform()
            .daemon()
            .start(runnable);
    }

    public void runAsyncFx(Runnable runnable) {
        runAsync(() -> Platform.runLater(runnable));
    }
}
