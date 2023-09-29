package res.model.exceptions;

public class ImpossibleToExtractLinesException extends NoStackTraceRuntimeException {
    public ImpossibleToExtractLinesException() {
        super("Il est impossible d'extraire les lignes du fichier");
    }
}
