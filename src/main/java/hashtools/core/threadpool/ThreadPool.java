package hashtools.core.threadpool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public enum ThreadPool {

    @SuppressWarnings("resource")
    CACHED_DAEMON(Executors.newCachedThreadPool(
        Thread.ofPlatform().daemon()::unstarted
    )),

    @SuppressWarnings("resource")
    FIXED_DAEMON(Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors(),
        Thread.ofPlatform().daemon()::unstarted
    ));



    private final ExecutorService threadPool;



    ThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }



    public <T> Future<T> submit(Callable<T> callable) {
        return threadPool.submit(callable);
    }

    public void execute(Runnable runnable) {
        threadPool.execute(runnable);
    }
}
