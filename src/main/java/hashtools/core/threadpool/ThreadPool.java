package hashtools.core.threadpool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public enum ThreadPool {

    @SuppressWarnings("resource")
    CACHED_DAEMON(createCached()),

    @SuppressWarnings("resource")
    FIXED_DAEMON(createFixed());



    private ExecutorService threadPool;



    ThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }



    private static ExecutorService createCached() {
        return Executors.newCachedThreadPool(
            Thread.ofPlatform().daemon()::unstarted
        );
    }

    private static ExecutorService createFixed() {
        return Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors(),
            Thread.ofPlatform().daemon()::unstarted
        );
    }

    public static void shutdown() {
        CACHED_DAEMON.threadPool.shutdownNow();
        FIXED_DAEMON.threadPool.shutdownNow();

        CACHED_DAEMON.threadPool = createCached();
        FIXED_DAEMON.threadPool = createFixed();
    }



    public <T> Future<T> submit(Callable<T> callable) {
        return threadPool.submit(callable);
    }

    public void execute(Runnable runnable) {
        threadPool.execute(runnable);
    }
}
