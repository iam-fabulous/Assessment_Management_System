//import assessment.manager.data.models.Assessment;
//import assessment.manager.data.models.Question;
//import assessment.manager.data.repositories.AssessmentRepo;
//import assessment.manager.data.repositories.QuestionRepo;
//import assessment.manager.dtos.requests.*;
//import assessment.manager.dtos.responses.AssessmentResponse;
//import assessment.manager.dtos.responses.QuestionResponse;
//import assessment.manager.exceptions.InvalidAssessmentRequestException;
//import assessment.manager.services.implementations.AssessmentServiceImpl;
//import assessment.manager.services.implementations.QuestionServiceImpl;
//import assessment.manager.services.interfaces.GradingService;
//import assessment.manager.services.interfaces.OptionService;
//import assessment.manager.services.interfaces.QuestionService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//public class AssessmentCompleteWorkflowIntegrationTest {
//
//    @Mock
//    private AssessmentRepo assessmentRepo;
//
//    @Mock
//    private QuestionRepo questionRepo;
//
//    @Mock
//    private QuestionService questionService;
//
//    @Mock
//    private OptionService optionService;
//
//    @Mock
//    private GradingService gradingService;
//
//    @InjectMocks
//    private AssessmentServiceImpl assessmentService;
//
//    @InjectMocks
//    private QuestionServiceImpl questionServiceImpl; // Real implementation for validation test
//
//    private String assessmentId = "assessment123";
//    private String creatorId = "creator123";
//    private Assessment mockAssessment;
//    private String questionId; // To store the generated UUID
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockAssessment = Assessment.builder()
//                .id(assessmentId)
//                .creatorId(creatorId)
//                .title("Java Assessment")
//                .description("Test Java skills")
//                .questionIds(new ArrayList<>())
//                .timerDuration(1800)
//                .isOptionsRandomize(false)
//                .isQuestionsRandomize(true)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//        setupMockRepositoryBehaviors();
//    }
//
//    private void setupMockRepositoryBehaviors() {
//        Map<String, Assessment> savedAssessments = new HashMap<>();
//        savedAssessments.put(assessmentId, mockAssessment); // Initialize with the original assessment
//
//        // Mock findById to return the latest saved version or the original if not updated
//        when(assessmentRepo.findById(anyString())).thenAnswer(invocation -> {
//            String id = invocation.getArgument(0);
//            return Optional.ofNullable(savedAssessments.getOrDefault(id, id.equals(assessmentId) ? mockAssessment : null));
//        });
//
//        // Mock save to update the saved state and return the updated assessment
//        when(assessmentRepo.save(any(Assessment.class))).thenAnswer(invocation -> {
//            Assessment a = invocation.getArgument(0);
//            if (a.getId() == null) {
//                a.setId(assessmentId); // Simulate ID assignment if null
//            }
//            savedAssessments.put(a.getId(), a); // Update the saved state with the new version
//            return a;
//        });
//
//        // Mock question existence dynamically if needed
//        when(questionService.questionExists(anyString())).thenAnswer(invocation -> {
//            String id = invocation.getArgument(0);
//            return id.equals(questionId); // Return true only for the created question ID
//        });
//    }
//
//    @Test
//    void testWorkflowWithValidationFailures() {
//        // Test with null text
//        CreateQuestionRequest invalidRequest = new CreateQuestionRequest();
//        assertThrows(InvalidAssessmentRequestException.class, () -> {
//            questionServiceImpl.createQuestion(invalidRequest); // Use real implementation
//        }, "Question text cannot be null or empty");
//
//        // Test with empty text
//        CreateQuestionRequest emptyRequest = new CreateQuestionRequest("", new ArrayList<>());
//        assertThrows(InvalidAssessmentRequestException.class, () -> {
//            questionServiceImpl.createQuestion(emptyRequest);
//        }, "Question text cannot be null or empty");
//    }
//
//    @Test
//    void testCompleteAssessmentWorkflow() {
//        // Create assessment
//        CreateAssessmentRequest createRequest = new CreateAssessmentRequest(
//                creatorId, "Java Assessment", "Test Java skills", 1800, false, true
//        );
//        AssessmentResponse createdResponse = assessmentService.createAssessment(createRequest);
//        assertNotNull(createdResponse.getId());
//
//        // Create question
//        CreateQuestionRequest questionRequest = new CreateQuestionRequest("Sample question", new ArrayList<>());
//        QuestionResponse questionResponse = questionServiceImpl.createQuestion(questionRequest); // Use real impl
//        questionId = questionResponse.getId(); // Capture the generated UUID
//        assertNotNull(questionId);
//
//        // Set timer
//        SetTimerRequest timerRequest = new SetTimerRequest(assessmentId, 45); // 45 minutes
//        assessmentService.setTimer(timerRequest);
//
//        // Retrieve and verify updated assessment
//        Assessment updatedAssessment = assessmentRepo.findById(assessmentId).orElseThrow();
//        assertEquals(2700, updatedAssessment.getTimerDuration()); // 45 * 60
//
//        // Add question
//        assessmentService.addQuestionToAssessment(assessmentId, questionId); // Use the captured ID
//        assertTrue(updatedAssessment.getQuestionIds().contains(questionId));
//    }
//}