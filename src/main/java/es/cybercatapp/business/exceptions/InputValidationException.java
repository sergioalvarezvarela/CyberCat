package es.cybercatapp.business.exceptions;


public class InputValidationException extends Exception {
    private static final long serialVersionUID = -4484026133900020199L;

    public InputValidationException(String message) {
        super(message);
    }
}
