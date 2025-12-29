package hash_tools.frontend.javafx;

import javafx.application.Platform;

public class AsyncRunner {

    public void runAsync(Runnable runnable) {
        Thread
            .ofPlatform()
            .daemon()
            .start(runnable);
    }

    public void runAsync(Runnable beforeTask, Runnable task, Runnable afterTask) {
        runAsync(() -> {
            try {
                beforeTask.run();
                task.run();
            } finally {
                afterTask.run();
            }
        });
    }

    public void runAsyncFx(Runnable runnable) {
        runAsync(() -> Platform.runLater(runnable));
    }
}
