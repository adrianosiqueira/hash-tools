package hashtools.core.domain;

public class ProblemException extends Exception {

    public ProblemException() {
    }

    public ProblemException(String message) {
        super(message);
    }

    public ProblemException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProblemException(Throwable cause) {
        super(cause);
    }

    public ProblemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
