package assessment.manager.exceptions;

/**
 * Exception thrown when an assessment request is invalid or contains invalid data
 */
public class InvalidAssessmentRequestException extends RuntimeException {
    public InvalidAssessmentRequestException(String message) {
        super(message);
    }
    
    public InvalidAssessmentRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}