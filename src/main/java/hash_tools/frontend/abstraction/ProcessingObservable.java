package hash_tools.frontend.abstraction;

public interface ProcessingObservable {

    void performWhenProcessingStarts(Runnable runnable);

    void performWhenProcessingStops(Runnable runnable);
}
