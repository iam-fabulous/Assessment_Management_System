package assessment.manager.services.interfaces;

import assessment.manager.data.models.Assessment;
import assessment.manager.dtos.requests.*;
import assessment.manager.dtos.responses.AssessmentListResponse;
import assessment.manager.dtos.responses.AssessmentResponse;
import assessment.manager.dtos.responses.GradeResponse;
import assessment.manager.dtos.responses.QuestionResponse;
import assessment.manager.dtos.responses.OptionResponse;

import java.util.List;
import java.util.Optional;

public interface AssessmentService {
    AssessmentResponse createAssessment(CreateAssessmentRequest request);
    AssessmentListResponse viewAssessmentsCreated(String creatorId);
    GradeResponse autoGrade(AutoGradeRequest request);
    void setTimer(SetTimerRequest request);
    void randomizeQuestions(RandomizeQuestionsRequest request);
    void randomizeOptions(RandomizeOptionsRequest request);
    void setCorrectAnswer(SetCorrectAnswerRequest request);
    
    // Question and Option management methods
    QuestionResponse createQuestion(CreateQuestionRequest request);
    OptionResponse createOption(CreateOptionRequest request);
    
    // Update methods
    AssessmentResponse updateAssessment(UpdateAssessmentRequest request);
    QuestionResponse updateQuestion(UpdateQuestionRequest request);
    OptionResponse updateOption(UpdateOptionRequest request);
    
    // Additional utility methods
    AssessmentResponse getAssessmentById(String assessmentId);
    void deleteAssessment(String assessmentId);
    void addQuestionToAssessment(String assessmentId, String questionId);
    void removeQuestionFromAssessment(String assessmentId, String questionId);
    
    // Question and Option utility methods
    QuestionResponse getQuestionResponseById(String questionId);
    OptionResponse getOptionResponseById(String optionId);
    void deleteQuestion(String questionId);
    void deleteOption(String optionId);
    
    // Additional query methods
    List<QuestionResponse> getAllQuestions();
    List<OptionResponse> getAllOptionsForQuestion(String questionId);
    AssessmentListResponse getAllAssessments();
}
