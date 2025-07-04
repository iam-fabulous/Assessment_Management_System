package assessment.manager.services.implementations;

import assessment.manager.data.models.Assessment;
import assessment.manager.data.models.Question;
import assessment.manager.dtos.requests.AutoGradeRequest;
import assessment.manager.dtos.requests.AnsweredQuestionRequest;
import assessment.manager.dtos.responses.GradeResponse;
import assessment.manager.exceptions.InvalidAssessmentRequestException;
import assessment.manager.exceptions.AssessmentNotFoundException;
import assessment.manager.exceptions.GradingException;
import assessment.manager.services.interfaces.GradingService;
import assessment.manager.services.interfaces.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradingServiceImpl implements GradingService {
    
    private final QuestionService questionService;
    
    @Override
    public GradeResponse autoGrade(AutoGradeRequest request) {
        try {
            validateGradeRequest(request);
            
            int totalQuestions = request.getAnsweredQuestions().size();
            int correctAnswers = 0;
            
            // Grade each answered question
            for (AnsweredQuestionRequest answeredQuestion : request.getAnsweredQuestions()) {
                Question question = questionService.getQuestionEntityById(answeredQuestion.getQuestionId());
                
                // Check if the selected answer is correct
                if (question.getCorrectOptionId() != null && 
                    question.getCorrectOptionId().equals(answeredQuestion.getSelectedOptionId())) {
                    correctAnswers++;
                }
            }
            
            // Calculate grade as percentage
            int grade = totalQuestions > 0 ? (correctAnswers * 100) / totalQuestions : 0;
            
            return GradeResponse.builder()
                    .grade(grade)
                    .totalQuestions(totalQuestions)
                    .correctAnswers(correctAnswers)
                    .build();
        } catch (Exception e) {
            throw new GradingException("Error grading assessment: " + request.getAssessmentId(), e);
        }
    }
    
    @Override
    public GradeResponse submitAndGradeAssessment(AutoGradeRequest gradeRequest) {
        try {
            validateGradeRequest(gradeRequest);
            
            // Perform auto-grading
            return autoGrade(gradeRequest);
        } catch (Exception e) {
            throw new GradingException("Error submitting and grading assessment: " + gradeRequest.getAssessmentId(), e);
        }
    }
    
    private void validateGradeRequest(AutoGradeRequest request) {
        if (request == null) {
            throw new InvalidAssessmentRequestException("Grading request cannot be null");
        }
        
        if (request.getAssessmentId() == null || request.getAssessmentId().trim().isEmpty()) {
            throw new InvalidAssessmentRequestException("Assessment ID cannot be null or empty");
        }
        
        if (request.getAnsweredQuestions() == null || request.getAnsweredQuestions().isEmpty()) {
            throw new InvalidAssessmentRequestException("Answered questions cannot be null or empty");
        }
        
        // Validate each answered question
        for (AnsweredQuestionRequest answeredQuestion : request.getAnsweredQuestions()) {
            if (answeredQuestion.getQuestionId() == null || answeredQuestion.getQuestionId().trim().isEmpty()) {
                throw new InvalidAssessmentRequestException("Question ID cannot be null or empty");
            }
            
            if (answeredQuestion.getSelectedOptionId() == null || answeredQuestion.getSelectedOptionId().trim().isEmpty()) {
                throw new InvalidAssessmentRequestException("Selected option ID cannot be null or empty");
            }
        }
    }
}