package hashtools.core.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolFactory {

    public static ExecutorService cachedDaemonPool() {
        return Executors.newCachedThreadPool(
            Thread.ofPlatform().daemon()::unstarted
        );
    }

    public static ExecutorService fixedDaemonPool() {
        return Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors(),
            Thread.ofPlatform().daemon()::unstarted
        );
    }
}
