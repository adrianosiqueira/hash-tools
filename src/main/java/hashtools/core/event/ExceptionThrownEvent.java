package hashtools.core.event;

public class ExceptionThrownEvent implements HashToolsEvent {

    private Throwable throwable;



    public ExceptionThrownEvent(Throwable throwable) {
        this.throwable = throwable;
    }



    public Throwable getThrowable() {
        return throwable;
    }
}
