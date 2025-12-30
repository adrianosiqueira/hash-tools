package hash_tools.frontend.javafx;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Execution {

    private List<Runnable> tasks = new ArrayList<>();
    private Consumer<Exception> exceptionHandler = this::doNotDoAnythingWithException;



    public Execution addTask(Runnable task) {
        tasks.add(task);
        return this;
    }

    public Execution addJavaFxTask(Runnable task) {
        tasks.add(() -> Platform.runLater(task));
        return this;
    }

    public Execution handleException(Consumer<Exception> exceptionHandler) {
        this.exceptionHandler = Optional
            .ofNullable(exceptionHandler)
            .orElse(this::doNotDoAnythingWithException);
        return this;
    }



    public void executeSequential() {
        Runnable runnable = () -> tasks.forEach(task -> {
            try {
                task.run();
            } catch (Exception e) {
                exceptionHandler.accept(e);
            }
        });

        Thread
            .ofPlatform()
            .daemon()
            .start(runnable);
    }



    private void doNotDoAnythingWithException(Exception unused) {
    }
}
