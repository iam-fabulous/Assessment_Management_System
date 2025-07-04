package assessment.manager.services.implementations;

import assessment.manager.data.models.Question;
import assessment.manager.data.repositories.QuestionRepo;
import assessment.manager.dtos.requests.CreateQuestionRequest;
import assessment.manager.dtos.requests.UpdateQuestionRequest;
import assessment.manager.dtos.requests.SetCorrectAnswerRequest;
import assessment.manager.dtos.responses.QuestionResponse;
import assessment.manager.dtos.responses.OptionResponse;
import assessment.manager.exceptions.InvalidAssessmentRequestException;
import assessment.manager.exceptions.QuestionNotFoundException;
import assessment.manager.services.interfaces.QuestionService;
import assessment.manager.services.interfaces.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    
    private final QuestionRepo questionRepo;
    private final OptionService optionService;
    
    @Override
    public QuestionResponse createQuestion(CreateQuestionRequest request) {
        try {
            validateCreateQuestionRequest(request);
            
            Question question = Question.builder()
                    .id(null)
                    .text(request.getText())
                    .optionIds(new ArrayList<>())
                    .correctOptionId(null)
                    .tags(request.getTags() != null ? request.getTags() : new ArrayList<>())
                    .build();
            
            Question savedQuestion = questionRepo.save(question);
            return convertToQuestionResponse(savedQuestion);
        } catch (Exception e) {
            throw new RuntimeException("Error creating question", e);
        }
    }
    
    @Override
    public QuestionResponse updateQuestion(UpdateQuestionRequest request) {
        try {
            validateUpdateQuestionRequest(request);
            
            Question existingQuestion = getQuestionEntityById(request.getQuestionId());
            
            Question updatedQuestion = Question.builder()
                    .id(existingQuestion.getId())
                    .text(request.getText())
                    .optionIds(existingQuestion.getOptionIds())
                    .correctOptionId(existingQuestion.getCorrectOptionId())
                    .tags(request.getTags() != null ? request.getTags() : existingQuestion.getTags())
                    .build();
            
            Question savedQuestion = questionRepo.save(updatedQuestion);
            return convertToQuestionResponse(savedQuestion);
        } catch (Exception e) {
            throw new RuntimeException("Error updating question: " + request.getQuestionId(), e);
        }
    }
    
    @Override
    public QuestionResponse getQuestionById(String questionId) {
        try {
            Question question = getQuestionEntityById(questionId);
            return convertToQuestionResponse(question);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching question: " + questionId, e);
        }
    }
    
    @Override
    public void deleteQuestion(String questionId) {
        try {
            Question question = getQuestionEntityById(questionId);
            
            // Delete all options associated with this question
            for (String optionId : question.getOptionIds()) {
                optionService.deleteOption(optionId);
            }
            
            // Delete the question
            questionRepo.deleteById(questionId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting question: " + questionId, e);
        }
    }
    
    @Override
    public List<QuestionResponse> getAllQuestions() {
        try {
            List<Question> questions = questionRepo.findAll();
            return questions.stream()
                    .map(this::convertToQuestionResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all questions", e);
        }
    }
    
    @Override
    public void setCorrectAnswer(SetCorrectAnswerRequest request) {
        try {
            Question question = getQuestionEntityById(request.getQuestionId());
            
            // Verify that the option exists and belongs to this question
            if (!question.getOptionIds().contains(request.getOptionId())) {
                throw new RuntimeException("Option ID " + request.getOptionId() + 
                    " does not belong to question ID " + request.getQuestionId());
            }
            
            // Verify the option exists
            if (!optionService.optionExists(request.getOptionId())) {
                throw new RuntimeException("Option not found with ID: " + request.getOptionId());
            }
            
            // Update the question with the correct answer
            Question updatedQuestion = Question.builder()
                    .id(question.getId())
                    .text(question.getText())
                    .optionIds(question.getOptionIds())
                    .correctOptionId(request.getOptionId())
                    .tags(question.getTags())
                    .build();
            
            questionRepo.save(updatedQuestion);
        } catch (Exception e) {
            throw new RuntimeException("Error setting correct answer for question: " + request.getQuestionId(), e);
        }
    }
    
    @Override
    public void addOptionToQuestion(String questionId, String optionId) {
        try {
            Question question = getQuestionEntityById(questionId);
            
            // Verify the option exists
            if (!optionService.optionExists(optionId)) {
                throw new RuntimeException("Option not found with ID: " + optionId);
            }
            
            // Add option if not already present
            if (!question.getOptionIds().contains(optionId)) {
                List<String> updatedOptionIds = new ArrayList<>(question.getOptionIds());
                updatedOptionIds.add(optionId);
                
                Question updatedQuestion = Question.builder()
                        .id(question.getId())
                        .text(question.getText())
                        .optionIds(updatedOptionIds)
                        .correctOptionId(question.getCorrectOptionId())
                        .tags(question.getTags())
                        .build();
                
                questionRepo.save(updatedQuestion);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error adding option to question: " + questionId, e);
        }
    }
    
    @Override
    public void removeOptionFromQuestion(String questionId, String optionId) {
        try {
            Question question = getQuestionEntityById(questionId);
            
            // Remove option if present
            if (question.getOptionIds().contains(optionId)) {
                List<String> updatedOptionIds = new ArrayList<>(question.getOptionIds());
                updatedOptionIds.remove(optionId);
                
                // If this was the correct option, clear the correct option
                String correctOptionId = question.getCorrectOptionId();
                if (optionId.equals(correctOptionId)) {
                    correctOptionId = null;
                }
                
                Question updatedQuestion = Question.builder()
                        .id(question.getId())
                        .text(question.getText())
                        .optionIds(updatedOptionIds)
                        .correctOptionId(correctOptionId)
                        .tags(question.getTags())
                        .build();
                
                questionRepo.save(updatedQuestion);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error removing option from question: " + questionId, e);
        }
    }
    
    @Override
    public Question getQuestionEntityById(String questionId) {
        return questionRepo.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException("Question not found with ID: " + questionId));
    }
    
    @Override
    public boolean questionExists(String questionId) {
        return questionRepo.existsById(questionId);
    }
    
    @Override
    public List<Question> getQuestionEntitiesByIds(List<String> questionIds) {
        return questionRepo.findAllById(questionIds);
    }
    
    private QuestionResponse convertToQuestionResponse(Question question) {
        List<OptionResponse> options = question.getOptionIds().stream()
                .map(optionService::getOptionById)
                .collect(Collectors.toList());
        
        return QuestionResponse.builder()
                .id(question.getId())
                .text(question.getText())
                .options(options)
                .correctOptionId(question.getCorrectOptionId())
                .tags(question.getTags())
                .build();
    }
    
    private void validateCreateQuestionRequest(CreateQuestionRequest request) {
        if (request == null) {
            throw new InvalidAssessmentRequestException("Question request cannot be null");
        }
        
        if (request.getText() == null || request.getText().trim().isEmpty()) {
            throw new InvalidAssessmentRequestException("Question text cannot be null or empty");
        }
        
        if (request.getText().length() > 1000) {
            throw new InvalidAssessmentRequestException("Question text cannot exceed 1000 characters");
        }
    }
    
    private void validateUpdateQuestionRequest(UpdateQuestionRequest request) {
        if (request == null) {
            throw new InvalidAssessmentRequestException("Question update request cannot be null");
        }
        
        if (request.getQuestionId() == null || request.getQuestionId().trim().isEmpty()) {
            throw new InvalidAssessmentRequestException("Question ID cannot be null or empty");
        }
        
        if (request.getText() == null || request.getText().trim().isEmpty()) {
            throw new InvalidAssessmentRequestException("Question text cannot be null or empty");
        }
        
        if (request.getText().length() > 1000) {
            throw new InvalidAssessmentRequestException("Question text cannot exceed 1000 characters");
        }
    }
}