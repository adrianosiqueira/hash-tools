package hashtools.service;

import hashtools.core.event.HashToolsEvent;
import hashtools.core.event.HashToolsEventBus;
import hashtools.core.event.HashToolsEventListener;
import hashtools.core.threadpool.ThreadPoolFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public enum EventService implements HashToolsEventBus {
    INSTANCE;



    private final Map<Class<?>, List<HashToolsEventListener<?>>> listenersMap;
    private final ExecutorService threadpool;



    EventService() {
        this.listenersMap = new ConcurrentHashMap<>();
        this.threadpool = ThreadPoolFactory.createDaemonPool();

        Runtime
            .getRuntime()
            .addShutdownHook(new Thread(threadpool::close));
    }



    @Override
    public <T extends HashToolsEvent> void register(Class<T> clazz, HashToolsEventListener<T> listener) {
        listenersMap
            .computeIfAbsent(clazz, _ -> Collections.synchronizedList(new ArrayList<>()))
            .add(listener);
    }

    @Override
    public <T extends HashToolsEvent> void unregister(Class<T> clazz, HashToolsEventListener<T> listener) {
        List<HashToolsEventListener<?>> listeners = listenersMap.get(clazz);

        if (listeners == null || listeners.isEmpty()) {
            // There is no listener to this event
            return;
        }

        listeners.remove(listener);
    }

    @Override
    public void publish(HashToolsEvent event) {
        List<HashToolsEventListener<?>> listeners = listenersMap.get(event.getClass());

        if (listeners == null || listeners.isEmpty()) {
            // There is no listener to this event
            return;
        }

        for (HashToolsEventListener<?> listener : listeners) {
            threadpool.execute(() -> {
                // noinspection unchecked
                HashToolsEventListener<HashToolsEvent> eventListener = ((HashToolsEventListener<HashToolsEvent>) listener);
                eventListener.reactTo(event);
            });
        }
    }
}
