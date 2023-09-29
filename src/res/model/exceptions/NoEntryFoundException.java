package res.model.exceptions;

public class NoEntryFoundException extends NoStackTraceRuntimeException {

    public NoEntryFoundException() {
        super("Aucun trou d'entrée n'a été trouvé");
    }
}
