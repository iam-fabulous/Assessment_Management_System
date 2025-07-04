package assessment.manager.exceptions;

/**
 * Exception thrown when an assessment operation fails
 */
public class AssessmentOperationException extends RuntimeException {
    public AssessmentOperationException(String message) {
        super(message);
    }
    
    public AssessmentOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}