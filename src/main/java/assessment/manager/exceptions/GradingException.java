package assessment.manager.exceptions;

/**
 * Exception thrown when an error occurs during assessment grading
 */
public class GradingException extends RuntimeException {
    public GradingException(String message) {
        super(message);
    }
    
    public GradingException(String message, Throwable cause) {
        super(message, cause);
    }
}