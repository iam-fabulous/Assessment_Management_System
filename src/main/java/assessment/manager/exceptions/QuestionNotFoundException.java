package assessment.manager.exceptions;

/**
 * Exception thrown when a question is not found
 */
public class QuestionNotFoundException extends RuntimeException {
    public QuestionNotFoundException(String message) {
        super(message);
    }
    
    public QuestionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}