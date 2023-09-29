package res.model.exceptions;

public class InvalidEntryException extends NoStackTraceRuntimeException {
    public InvalidEntryException() {
        super("Les paramètres fournis sont incorrects.");
    }
}

