package res.model.exceptions;

public class ThreadConcurrentException extends NoStackTraceRuntimeException {
    public ThreadConcurrentException() {
        super("Impossible de faire un wait");
    }
}
