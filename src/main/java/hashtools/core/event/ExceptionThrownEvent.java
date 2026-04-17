package hashtools.core.event;

import hashtools.core.model.ThrowableWrapper;

public class ExceptionThrownEvent implements HashToolsEvent {

    private ThrowableWrapper throwable;



    public ExceptionThrownEvent(Throwable throwable) {
        this.throwable = new ThrowableWrapper(throwable);
    }

    public ExceptionThrownEvent(ThrowableWrapper throwable) {
        this.throwable = throwable;
    }



    public Throwable getThrowable() {
        return throwable.getThrowable();
    }

    public String getStackTrace() {
        return throwable.getStackTrace();
    }
}
