package assessment.manager.exceptions;

/**
 * Exception thrown when an option is not found
 */
public class OptionNotFoundException extends RuntimeException {
    public OptionNotFoundException(String message) {
        super(message);
    }
    
    public OptionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}