package assessment.manager.services.interfaces;

import assessment.manager.dtos.requests.CreateQuestionRequest;
import assessment.manager.dtos.requests.UpdateQuestionRequest;
import assessment.manager.dtos.requests.SetCorrectAnswerRequest;
import assessment.manager.dtos.responses.QuestionResponse;
import assessment.manager.data.models.Question;

import java.util.List;

public interface QuestionService {
    
    // Core CRUD operations
    QuestionResponse createQuestion(CreateQuestionRequest request);
    QuestionResponse updateQuestion(UpdateQuestionRequest request);
    QuestionResponse getQuestionById(String questionId);
    void deleteQuestion(String questionId);
    List<QuestionResponse> getAllQuestions();
    
    // Business logic operations
    void setCorrectAnswer(SetCorrectAnswerRequest request);
    void addOptionToQuestion(String questionId, String optionId);
    void removeOptionFromQuestion(String questionId, String optionId);
    
    // Internal operations (used by other services)
    Question getQuestionEntityById(String questionId);
    boolean questionExists(String questionId);
    List<Question> getQuestionEntitiesByIds(List<String> questionIds);
}