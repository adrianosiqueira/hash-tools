package hashtools.service;

import hashtools.core.event.HashToolsEvent;
import hashtools.core.event.HashToolsEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum EventService {
    INSTANCE;



    private final Map<Class<?>, List<HashToolsEventListener<?>>> listenersMap;
    private final ExecutorService threadpool;



    EventService() {
        this.listenersMap = new ConcurrentHashMap<>();
        this.threadpool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }



    public <T extends HashToolsEvent> void register(Class<T> clazz, HashToolsEventListener<T> listener) {
        listenersMap
            .computeIfAbsent(clazz, _ -> Collections.synchronizedList(new ArrayList<>()))
            .add(listener);
    }

    public void dispatch(HashToolsEvent event) {
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



    public void shutdown() {
        threadpool.close();
    }
}
