package exceptions;

public class EmptyProjectException extends Exception {
    /**
     * Constructeur pour la classe {@link EmptyProjectException}
     * @param message le message à afficher lorsque l'exception EmptyProjectException est levée.
     */
    public EmptyProjectException(String message) {
        super(message);
    }
}
