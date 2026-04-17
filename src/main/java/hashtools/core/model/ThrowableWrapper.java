package hashtools.core.model;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.function.Function;

public class ThrowableWrapper {

    private final Throwable throwable;



    public ThrowableWrapper(Throwable throwable) {
        this.throwable = throwable;
    }



    public Throwable getThrowable() {
        return throwable;
    }

    public String getStackTrace() {
        Function<Throwable, String> stackTraceRetrieveFunction = throwable -> {
            StringWriter stackTrace = new StringWriter();

            PrintWriter printWriter = new PrintWriter(stackTrace);
            throwable.printStackTrace(printWriter);

            return stackTrace.toString();
        };



        return Optional
            .ofNullable(throwable)
            .map(stackTraceRetrieveFunction)
            .orElse("");
    }

    public String getMessage() {
        return Optional
            .ofNullable(throwable)
            .map(Throwable::getMessage)
            .orElse("");
    }

    public String getSimpleClassName() {
        return Optional
            .ofNullable(throwable)
            .map(Throwable::getClass)
            .map(Class::getSimpleName)
            .orElse("");
    }
}
