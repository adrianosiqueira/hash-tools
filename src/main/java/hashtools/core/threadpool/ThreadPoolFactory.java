package hashtools.core.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolFactory {

    public static ExecutorService createDaemonPool() {
        return Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors(),
            new DaemonThreadFactory()
        );
    }
}
