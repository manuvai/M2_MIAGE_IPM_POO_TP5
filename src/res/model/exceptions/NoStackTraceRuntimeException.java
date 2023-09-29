package res.model.exceptions;

public class NoStackTraceRuntimeException extends RuntimeException {
    public NoStackTraceRuntimeException(String message) {
        super(message);
    }
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}

