package exceptions;

public class NotFoundPathProjectException extends Exception {
    /**
     * Constructeur pour la classe {@link NotFoundPathProjectException}
     * @param message le message à afficher lorsque l'exception NotFoundPathProjectException est levée.
     */
    public NotFoundPathProjectException(String message) {
        super(message);
    }
}
