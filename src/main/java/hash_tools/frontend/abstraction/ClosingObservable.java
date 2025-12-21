package hash_tools.frontend.abstraction;

public interface ClosingObservable {

    void performWhenClosed(Runnable runnable);

    void performClosingTasks();
}
