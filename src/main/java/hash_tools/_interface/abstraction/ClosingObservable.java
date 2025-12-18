package hash_tools._interface.abstraction;

public interface ClosingObservable {

    void performWhenClosed(Runnable runnable);

    void performClosingTasks();
}
