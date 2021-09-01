package be.dimitrigevers.jdbc.exceptions;

public class SoortAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SoortAlreadyExistsException(String message) {
        super(message);
    }
}
