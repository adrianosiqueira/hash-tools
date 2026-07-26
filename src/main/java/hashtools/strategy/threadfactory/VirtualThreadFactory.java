package hashtools.strategy.threadfactory;

import java.util.concurrent.ThreadFactory;

public record VirtualThreadFactory() implements ThreadFactory {

    @Override
    public Thread newThread(Runnable runnable) {
        return Thread
            .ofVirtual()
            .unstarted(runnable);
    }
}
