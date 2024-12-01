package exceptions;

public class InvalidContentException extends Exception {
    private static final long serialVersionUID = 1L;

	public InvalidContentException(String message) {
        super(message);
    }
}
