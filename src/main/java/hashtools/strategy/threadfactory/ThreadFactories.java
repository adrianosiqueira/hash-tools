package hashtools.strategy.threadfactory;

public class ThreadFactories {

    public static Thread newVirtual(Runnable runnable) {
        return Thread
            .ofVirtual()
            .unstarted(runnable);
    }

    public static Thread newPlatformDaemon(Runnable runnable) {
        return Thread
            .ofPlatform()
            .daemon()
            .unstarted(runnable);
    }
}
