package hashtools.core.event;

public interface HashToolsEventBus {

    <T extends HashToolsEvent> void register(Class<T> clazz, HashToolsEventListener<T> reactor);

    void publish(HashToolsEvent event);
}
