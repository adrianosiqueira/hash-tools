package hashtools.core.threadpool;

import java.util.concurrent.ThreadFactory;

public class DaemonThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable runnable) {
        return Thread
            .ofPlatform()
            .daemon()
            .unstarted(runnable);
    }
}
