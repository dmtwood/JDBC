package be.dimitrigevers.jdbc.exceptions;

public class SoortNotFoundException extends RuntimeException {


    private static final long serialVersionUID = 1L;

    public SoortNotFoundException(String message) {
        super(message);
    }
}
