package assessment.manager.services.interfaces;

import assessment.manager.dtos.requests.AutoGradeRequest;
import assessment.manager.dtos.responses.GradeResponse;

public interface GradingService {
    
    /**
     * Automatically grade an assessment based on the provided answers
     * @param request The grading request containing assessment ID and answers
     * @return GradeResponse containing the calculated grade
     */
    GradeResponse autoGrade(AutoGradeRequest request);
    
    /**
     * Submit assessment and auto-grade it
     * @param gradeRequest The grading request
     * @return GradeResponse containing the calculated grade
     */
    GradeResponse submitAndGradeAssessment(AutoGradeRequest gradeRequest);
}