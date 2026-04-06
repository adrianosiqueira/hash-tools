package hashtools.core.event;

public interface HashToolsEventListener<T extends HashToolsEvent> {

    void reactTo(T event);
}
