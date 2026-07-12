package hashtools.backend.core.strategy.threadpool;

import hashtools.backend.core.interfaces.ThreadPool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AllCoreDaemonThreadPool implements ThreadPool {

    private ExecutorService executor;



    public AllCoreDaemonThreadPool() {
        this.executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors(),
            Thread.ofPlatform().daemon()::unstarted
        );
    }



    @Override
    public void run(Runnable task) {
        executor.execute(task);
    }

    @Override
    public <R> Future<R> run(Callable<R> task) {
        return executor.submit(task);
    }

    @Override
    public void close() {
        executor.shutdownNow();
    }
}
