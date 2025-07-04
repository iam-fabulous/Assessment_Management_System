package assessment.manager.exceptions;

/**
 * Exception thrown when an assessment is not found
 */
public class AssessmentNotFoundException extends RuntimeException {
    public AssessmentNotFoundException(String message) {
        super(message);
    }
    
    public AssessmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}