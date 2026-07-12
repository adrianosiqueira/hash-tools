package hashtools.backend.core.interfaces;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface ThreadPool extends AutoCloseable {

    void run(Runnable task);

    <R> Future<R> run(Callable<R> task);

    void close();
}
