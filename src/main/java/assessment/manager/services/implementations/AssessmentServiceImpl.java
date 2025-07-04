package assessment.manager.services.implementations;

import assessment.manager.data.models.Assessment;
import assessment.manager.data.repositories.AssessmentRepo;
import assessment.manager.dtos.requests.*;
import assessment.manager.dtos.responses.*;
import assessment.manager.exceptions.*;
import assessment.manager.services.interfaces.AssessmentService;
import assessment.manager.services.interfaces.QuestionService;
import assessment.manager.services.interfaces.OptionService;
import assessment.manager.services.interfaces.GradingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssessmentServiceImpl implements AssessmentService {
    
    private final AssessmentRepo assessmentRepo;
    private final QuestionService questionService;
    private final OptionService optionService;
    private final GradingService gradingService;
    
    // ===================== CORE ASSESSMENT OPERATIONS =====================
    
    @Override
    public AssessmentResponse createAssessment(CreateAssessmentRequest request) {
        validateCreateAssessmentRequest(request);
        
        Assessment assessment = Assessment.builder()
                .id(null)
                .creatorId(request.getCreatorId())
                .title(request.getTitle())
                .description(request.getDescription())
                .questionIds(new ArrayList<>())
                .timerDuration(request.getTimerDuration())
                .isOptionsRandomize(request.isRandomizeOptions())
                .isQuestionsRandomize(request.isRandomizeQuestions())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        Assessment savedAssessment = assessmentRepo.save(assessment);
        return convertToAssessmentResponse(savedAssessment);
    }

    @Override
    public AssessmentListResponse viewAssessmentsCreated(String creatorId) {
        if (creatorId == null || creatorId.trim().isEmpty()) {
            throw new InvalidAssessmentRequestException("Creator ID cannot be null or empty");
        }
        
        try {
            List<Assessment> assessments = assessmentRepo.findByCreatorId(creatorId);
            
            List<AssessmentResponse> assessmentResponses = assessments.stream()
                    .map(this::convertToAssessmentResponse)
                    .collect(Collectors.toList());
            
            return AssessmentListResponse.builder()
                    .assessments(assessmentResponses)
                    .build();
        } catch (Exception e) {
            throw new AssessmentOperationException("Error fetching assessments for creator: " + creatorId, e);
        }
    }

    @Override
    public AssessmentResponse updateAssessment(UpdateAssessmentRequest request) {
        try {
            validateUpdateAssessmentRequest(request);
            
            Assessment existingAssessment = getAssessmentEntityById(request.getAssessmentId());
            
            Assessment updatedAssessment = Assessment.builder()
                    .id(existingAssessment.getId())
                    .creatorId(existingAssessment.getCreatorId())
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .questionIds(existingAssessment.getQuestionIds())
                    .timerDuration(request.getTimerDuration())
                    .isOptionsRandomize(request.isRandomizeOptions())
                    .isQuestionsRandomize(request.isRandomizeQuestions())
                    .createdAt(existingAssessment.getCreatedAt())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            Assessment savedAssessment = assessmentRepo.save(updatedAssessment);
            return convertToAssessmentResponse(savedAssessment);
        } catch (Exception e) {
            throw new AssessmentOperationException("Error updating assessment: " + request.getAssessmentId(), e);
        }
    }
    
    @Override
    public AssessmentResponse getAssessmentById(String assessmentId) {
        try {
            Assessment assessment = getAssessmentEntityById(assessmentId);
            return convertToAssessmentResponse(assessment);
        } catch (Exception e) {
            throw new AssessmentOperationException("Error fetching assessment: " + assessmentId, e);
        }
    }
    
    @Override
    public void deleteAssessment(String assessmentId) {
        try {
            if (!assessmentRepo.existsById(assessmentId)) {
                throw new AssessmentNotFoundException("Assessment not found with ID: " + assessmentId);
            }
            assessmentRepo.deleteById(assessmentId);
        } catch (Exception e) {
            throw new AssessmentOperationException("Error deleting assessment: " + assessmentId, e);
        }
    }
    
    // ===================== ASSESSMENT CONFIGURATION OPERATIONS =====================

    @Override
    public void setTimer(SetTimerRequest request) {
        try {
            Assessment assessment = getAssessmentEntityById(request.getAssessmentId());
            
            long timerDurationInSeconds = request.getDurationMinutes() * 60;
            
            Assessment updatedAssessment = Assessment.builder()
                    .id(assessment.getId())
                    .creatorId(assessment.getCreatorId())
                    .title(assessment.getTitle())
                    .description(assessment.getDescription())
                    .questionIds(assessment.getQuestionIds())
                    .timerDuration(timerDurationInSeconds)
                    .isOptionsRandomize(assessment.isOptionsRandomize())
                    .isQuestionsRandomize(assessment.isQuestionsRandomize())
                    .createdAt(assessment.getCreatedAt())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            assessmentRepo.save(updatedAssessment);
        } catch (Exception e) {
            throw new AssessmentOperationException("Error setting timer for assessment: " + request.getAssessmentId(), e);
        }
    }

    @Override
    public void randomizeQuestions(RandomizeQuestionsRequest request) {
        try {
            Assessment assessment = getAssessmentEntityById(request.getAssessmentId());
            
            Assessment updatedAssessment = Assessment.builder()
                    .id(assessment.getId())
                    .creatorId(assessment.getCreatorId())
                    .title(assessment.getTitle())
                    .description(assessment.getDescription())
                    .questionIds(assessment.getQuestionIds())
                    .timerDuration(assessment.getTimerDuration())
                    .isOptionsRandomize(assessment.isOptionsRandomize())
                    .isQuestionsRandomize(!assessment.isQuestionsRandomize())
                    .createdAt(assessment.getCreatedAt())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            assessmentRepo.save(updatedAssessment);
        } catch (Exception e) {
            throw new AssessmentOperationException("Error updating question randomization for assessment: " + request.getAssessmentId(), e);
        }
    }

    @Override
    public void randomizeOptions(RandomizeOptionsRequest request) {
        try {
            Assessment assessment = getAssessmentEntityById(request.getAssessmentId());
            
            Assessment updatedAssessment = Assessment.builder()
                    .id(assessment.getId())
                    .creatorId(assessment.getCreatorId())
                    .title(assessment.getTitle())
                    .description(assessment.getDescription())
                    .questionIds(assessment.getQuestionIds())
                    .timerDuration(assessment.getTimerDuration())
                    .isOptionsRandomize(!assessment.isOptionsRandomize())
                    .isQuestionsRandomize(assessment.isQuestionsRandomize())
                    .createdAt(assessment.getCreatedAt())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            assessmentRepo.save(updatedAssessment);
        } catch (Exception e) {
            throw new AssessmentOperationException("Error updating option randomization for assessment: " + request.getAssessmentId(), e);
        }
    }
    
    // ===================== QUESTION MANAGEMENT OPERATIONS =====================

    @Override
    public void addQuestionToAssessment(String assessmentId, String questionId) {
        try {
            Assessment assessment = getAssessmentEntityById(assessmentId);
            
            // Verify the question exists
            if (!questionService.questionExists(questionId)) {
                throw new RuntimeException("Question not found with ID: " + questionId);
            }
            
            // Add question if not already present
            if (!assessment.getQuestionIds().contains(questionId)) {
                List<String> updatedQuestionIds = new ArrayList<>(assessment.getQuestionIds());
                updatedQuestionIds.add(questionId);
                
                Assessment updatedAssessment = Assessment.builder()
                        .id(assessment.getId())
                        .creatorId(assessment.getCreatorId())
                        .title(assessment.getTitle())
                        .description(assessment.getDescription())
                        .questionIds(updatedQuestionIds)
                        .timerDuration(assessment.getTimerDuration())
                        .isOptionsRandomize(assessment.isOptionsRandomize())
                        .isQuestionsRandomize(assessment.isQuestionsRandomize())
                        .createdAt(assessment.getCreatedAt())
                        .updatedAt(LocalDateTime.now())
                        .build();
                
                assessmentRepo.save(updatedAssessment);
            }
        } catch (Exception e) {
            throw new AssessmentOperationException("Error adding question to assessment: " + assessmentId, e);
        }
    }
    
    @Override
    public void removeQuestionFromAssessment(String assessmentId, String questionId) {
        try {
            Assessment assessment = getAssessmentEntityById(assessmentId);
            
            // Remove question if present
            if (assessment.getQuestionIds().contains(questionId)) {
                List<String> updatedQuestionIds = new ArrayList<>(assessment.getQuestionIds());
                updatedQuestionIds.remove(questionId);
                
                Assessment updatedAssessment = Assessment.builder()
                        .id(assessment.getId())
                        .creatorId(assessment.getCreatorId())
                        .title(assessment.getTitle())
                        .description(assessment.getDescription())
                        .questionIds(updatedQuestionIds)
                        .timerDuration(assessment.getTimerDuration())
                        .isOptionsRandomize(assessment.isOptionsRandomize())
                        .isQuestionsRandomize(assessment.isQuestionsRandomize())
                        .createdAt(assessment.getCreatedAt())
                        .updatedAt(LocalDateTime.now())
                        .build();
                
                assessmentRepo.save(updatedAssessment);
            }
        } catch (Exception e) {
            throw new AssessmentOperationException("Error removing question from assessment: " + assessmentId, e);
        }
    }
    
    // ===================== DELEGATE TO OTHER SERVICES =====================

    @Override
    public GradeResponse autoGrade(AutoGradeRequest request) {
        return gradingService.autoGrade(request);
    }
    
    @Override
    public void setCorrectAnswer(SetCorrectAnswerRequest request) {
        questionService.setCorrectAnswer(request);
    }
    
    @Override
    public QuestionResponse createQuestion(CreateQuestionRequest request) {
        return questionService.createQuestion(request);
    }

    @Override
    public OptionResponse createOption(CreateOptionRequest request) {
        OptionResponse option = optionService.createOption(request);
        // Add the option to the question
        questionService.addOptionToQuestion(request.getQuestionId(), option.getId());
        return option;
    }

    @Override
    public QuestionResponse updateQuestion(UpdateQuestionRequest request) {
        return questionService.updateQuestion(request);
    }
    
    @Override
    public OptionResponse updateOption(UpdateOptionRequest request) {
        return optionService.updateOption(request);
    }
    
    @Override
    public QuestionResponse getQuestionResponseById(String questionId) {
        return questionService.getQuestionById(questionId);
    }
    
    @Override
    public OptionResponse getOptionResponseById(String optionId) {
        return optionService.getOptionById(optionId);
    }
    
    @Override
    public void deleteQuestion(String questionId) {
        // Remove question from any assessments first
        List<Assessment> assessments = assessmentRepo.findAll();
        for (Assessment assessment : assessments) {
            if (assessment.getQuestionIds().contains(questionId)) {
                removeQuestionFromAssessment(assessment.getId(), questionId);
            }
        }
        
        // Delete the question (and its options)
        questionService.deleteQuestion(questionId);
    }
    
    @Override
    public void deleteOption(String optionId) {
        optionService.deleteOption(optionId);
    }
    
    @Override
    public List<QuestionResponse> getAllQuestions() {
        return questionService.getAllQuestions();
    }
    
    @Override
    public List<OptionResponse> getAllOptionsForQuestion(String questionId) {
        return optionService.getAllOptionsForQuestion(questionId);
    }
    
    // ===================== PRIVATE HELPER METHODS =====================

    private Assessment getAssessmentEntityById(String assessmentId) {
        return assessmentRepo.findById(assessmentId)
                .orElseThrow(() -> new AssessmentNotFoundException("Assessment not found with ID: " + assessmentId));
    }
    
    private AssessmentResponse convertToAssessmentResponse(Assessment assessment) {
        List<QuestionResponse> questions = assessment.getQuestionIds().stream()
                .map(questionService::getQuestionById)
                .collect(Collectors.toList());
        
        return AssessmentResponse.builder()
                .id(assessment.getId())
                .creatorId(assessment.getCreatorId())
                .title(assessment.getTitle())
                .description(assessment.getDescription())
                .questions(questions)
                .timerDuration(assessment.getTimerDuration())
                .isQuestionsRandomized(assessment.isQuestionsRandomize())
                .isOptionsRandomized(assessment.isOptionsRandomize())
                .createdAt(assessment.getCreatedAt())
                .updatedAt(assessment.getUpdatedAt())
                .build();
    }
    
    // ===================== VALIDATION METHODS =====================

    private void validateCreateAssessmentRequest(CreateAssessmentRequest request) {
        if (request == null) {
            throw new InvalidAssessmentRequestException("Assessment request cannot be null");
        }
        
        if (request.getCreatorId() == null || request.getCreatorId().trim().isEmpty()) {
            throw new InvalidAssessmentRequestException("Creator ID cannot be null or empty");
        }
        
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new InvalidAssessmentRequestException("Title cannot be null or empty");
        }
        
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new InvalidAssessmentRequestException("Description cannot be null or empty");
        }
        
        if (request.getTimerDuration() <= 0) {
            throw new InvalidAssessmentRequestException("Timer duration must be positive (greater than 0 seconds)");
        }
    }
    
    private void validateUpdateAssessmentRequest(UpdateAssessmentRequest request) {
        if (request == null) {
            throw new InvalidAssessmentRequestException("Assessment update request cannot be null");
        }
        
        if (request.getAssessmentId() == null || request.getAssessmentId().trim().isEmpty()) {
            throw new InvalidAssessmentRequestException("Assessment ID cannot be null or empty");
        }
        
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new InvalidAssessmentRequestException("Title cannot be null or empty");
        }
        
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new InvalidAssessmentRequestException("Description cannot be null or empty");
        }
        
        if (request.getTimerDuration() <= 0) {
            throw new InvalidAssessmentRequestException("Timer duration must be positive (greater than 0 seconds)");
        }
    }
    
    // ===================== ADDITIONAL QUERY METHODS =====================
    
    @Override
    public AssessmentListResponse getAllAssessments() {
        try {
            List<Assessment> assessments = assessmentRepo.findAll();
            
            List<AssessmentResponse> assessmentResponses = assessments.stream()
                    .map(this::convertToAssessmentResponse)
                    .collect(Collectors.toList());
            
            return AssessmentListResponse.builder()
                    .assessments(assessmentResponses)
                    .totalCount(assessmentResponses.size())
                    .build();
        } catch (Exception e) {
            throw new AssessmentOperationException("Error fetching all assessments", e);
        }
    }
}
