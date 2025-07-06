import assessment.manager.data.models.Assessment;
import assessment.manager.data.repositories.AssessmentRepo;
import assessment.manager.dtos.requests.*;
import assessment.manager.dtos.responses.*;
import assessment.manager.exceptions.AssessmentNotFoundException;
import assessment.manager.exceptions.AssessmentOperationException;
import assessment.manager.exceptions.InvalidAssessmentRequestException;
import assessment.manager.services.implementations.AssessmentServiceImpl;
import assessment.manager.services.interfaces.GradingService;
import assessment.manager.services.interfaces.OptionService;
import assessment.manager.services.interfaces.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssessmentServiceImplTest {

    @Mock
    private AssessmentRepo assessmentRepo;

    @Mock
    private QuestionService questionService;

    @Mock
    private OptionService optionService;

    @Mock
    private GradingService gradingService;

    @InjectMocks
    private AssessmentServiceImpl assessmentService;

    private String creatorId;
    private String assessmentId;
    private Assessment mockAssessment;

    @BeforeEach
    void setUp() {
        creatorId = "creator123";
        assessmentId = "assessment123";

        mockAssessment = Assessment.builder()
                .id(assessmentId)
                .creatorId(creatorId)
                .title("Java Assessment")
                .description("Test Java skills")
                .questionIds(new ArrayList<>())
                .timerDuration(1800)
                .isOptionsRandomize(false)
                .isQuestionsRandomize(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // ===================== CORE ASSESSMENT OPERATIONS =====================

    @Test
    void testCreateAssessment_Success() {
        CreateAssessmentRequest request = new CreateAssessmentRequest(
                creatorId, "Java Assessment", "Test Java skills", 1800, false, true
        );
        when(assessmentRepo.save(any(Assessment.class))).thenAnswer(invocation -> {
            Assessment a = invocation.getArgument(0);
            a.setId(assessmentId); // Simulate ID assignment
            return a;
        });

        AssessmentResponse response = assessmentService.createAssessment(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(assessmentId, response.getId());
        assertEquals(creatorId, response.getCreatorId());
        assertEquals("Java Assessment", response.getTitle());
        assertEquals(1800, response.getTimerDuration());
        assertFalse(response.isOptionsRandomized());
        assertTrue(response.isQuestionsRandomized());
        verify(assessmentRepo).save(any(Assessment.class));
    }

    @Test
    void testCreateAssessment_NullRequest() {
        assertThrows(InvalidAssessmentRequestException.class, () -> {
            assessmentService.createAssessment(null);
        }, "Assessment request cannot be null");
    }

    @Test
    void testCreateAssessment_InvalidCreatorId() {
        CreateAssessmentRequest request = new CreateAssessmentRequest(
                null, "Java Assessment", "Test Java skills", 1800, false, true
        );
        assertThrows(InvalidAssessmentRequestException.class, () -> {
            assessmentService.createAssessment(request);
        }, "Creator ID cannot be null or empty");
    }

    @Test
    void testViewAssessmentsCreated_Success() {
        when(assessmentRepo.findByCreatorId(creatorId)).thenReturn(List.of(mockAssessment));
        AssessmentListResponse response = assessmentService.viewAssessmentsCreated(creatorId);

        assertNotNull(response);
        assertNotNull(response.getAssessments());
        assertEquals(1, response.getAssessments().size());
        assertEquals("Java Assessment", response.getAssessments().get(0).getTitle());
        verify(assessmentRepo).findByCreatorId(creatorId);
    }

    @Test
    void testUpdateAssessment_Success() {
        when(assessmentRepo.findById(assessmentId)).thenReturn(Optional.of(mockAssessment));
        UpdateAssessmentRequest request = new UpdateAssessmentRequest(
                assessmentId, "Updated Java Assessment", "Updated description", 3600, false, true
        );
        when(assessmentRepo.save(any(Assessment.class))).thenAnswer(invocation -> {
            Assessment updatedAssessment = invocation.getArgument(0);
            return updatedAssessment; // Return the updated Assessment
        });

        AssessmentResponse response = assessmentService.updateAssessment(request);

        assertNotNull(response);
        assertEquals(assessmentId, response.getId());
        assertEquals("Updated Java Assessment", response.getTitle()); // Should now pass
        assertEquals(3600, response.getTimerDuration());
        verify(assessmentRepo).save(any(Assessment.class));
    }

    @Test
    void testUpdateAssessment_NullRequest() {
        assertThrows(InvalidAssessmentRequestException.class, () -> {
            assessmentService.updateAssessment(null);
        }, "Assessment update request cannot be null");
    }

    @Test
    void testUpdateAssessment_InvalidAssessmentId() {
        UpdateAssessmentRequest request = new UpdateAssessmentRequest(
                null, "Updated Java Assessment", "Updated description", 3600, false, true
        );
        assertThrows(InvalidAssessmentRequestException.class, () -> {
            assessmentService.updateAssessment(request);
        }, "Assessment ID cannot be null or empty");
    }

    @Test
    void testUpdateAssessment_NotFound() {
        when(assessmentRepo.findById("nonExistentId")).thenReturn(Optional.empty());
        UpdateAssessmentRequest request = new UpdateAssessmentRequest(
                "nonExistentId", "Updated Java Assessment", "Updated description", 3600, false, true
        );
        assertThrows(AssessmentNotFoundException.class, () -> {
            assessmentService.updateAssessment(request);
        }, "Assessment not found with ID: nonExistentId");
    }

    @Test
    void testGetAssessmentById_Success() {
        when(assessmentRepo.findById(assessmentId)).thenReturn(Optional.of(mockAssessment));
        AssessmentResponse response = assessmentService.getAssessmentById(assessmentId);

        assertNotNull(response);
        assertEquals(assessmentId, response.getId());
        assertEquals("Java Assessment", response.getTitle());
        verify(assessmentRepo).findById(assessmentId);
    }

    @Test
    void testGetAssessmentById_NotFound() {
        when(assessmentRepo.findById("nonExistentId")).thenReturn(Optional.empty());
        assertThrows(AssessmentOperationException.class, () -> {
            assessmentService.getAssessmentById("nonExistentId");
        }, "Assessment not found with ID: nonExistentId");
    }

    @Test
    void testDeleteAssessment_Success() {
        when(assessmentRepo.existsById(assessmentId)).thenReturn(Boolean.valueOf(true));
        assessmentService.deleteAssessment(assessmentId);
        verify(assessmentRepo).deleteById(assessmentId);
    }

    @Test
    void testDeleteAssessment_NotFound() {
        when(assessmentRepo.existsById("nonExistentId")).thenReturn(Boolean.valueOf(false));
        assertThrows(AssessmentOperationException.class, () -> {
            assessmentService.deleteAssessment("nonExistentId");
        }, "Assessment not found with ID: nonExistentId");
    }

    // ===================== ASSESSMENT CONFIGURATION OPERATIONS =====================

    @Test
    void testSetTimer_Success() {
        when(assessmentRepo.findById(assessmentId)).thenReturn(Optional.of(mockAssessment));
        SetTimerRequest request = new SetTimerRequest(assessmentId, 45); // 45 minutes
        when(assessmentRepo.save(any(Assessment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assessmentService.setTimer(request);

        verify(assessmentRepo).save(argThat(a -> a.getTimerDuration() == 2700L));
    }

    @Test
    void testSetTimer_NotFound() {
        when(assessmentRepo.findById("nonExistentId")).thenReturn(Optional.empty());
        SetTimerRequest request = new SetTimerRequest("nonExistentId", 45);
        assertThrows(AssessmentNotFoundException.class, () -> {
            assessmentService.setTimer(request);
        }, "Assessment not found with ID: nonExistentId");
    }

    @Test
    void testRandomizeQuestions_Success() {
        when(assessmentRepo.findById(assessmentId)).thenReturn(Optional.of(mockAssessment));
        RandomizeQuestionsRequest request = new RandomizeQuestionsRequest(assessmentId);
        when(assessmentRepo.save(any(Assessment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assessmentService.randomizeQuestions(request);

        verify(assessmentRepo).save(argThat(a -> !a.isQuestionsRandomize()));
    }

    @Test
    void testRandomizeQuestions_NotFound() {
        when(assessmentRepo.findById("nonExistentId")).thenReturn(Optional.empty());
        RandomizeQuestionsRequest request = new RandomizeQuestionsRequest("nonExistentId");
        assertThrows(AssessmentOperationException.class, () -> {
            assessmentService.randomizeQuestions(request);
        }, "Assessment not found with ID: nonExistentId");
    }

    @Test
    void testRandomizeOptions_Success() {
        when(assessmentRepo.findById(assessmentId)).thenReturn(Optional.of(mockAssessment));
        RandomizeOptionsRequest request = new RandomizeOptionsRequest(assessmentId);
        when(assessmentRepo.save(any(Assessment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assessmentService.randomizeOptions(request);

        verify(assessmentRepo).save(argThat(a -> a.isOptionsRandomize()));
    }

    @Test
    void testRandomizeOptions_NotFound() {
        when(assessmentRepo.findById("nonExistentId")).thenReturn(Optional.empty());
        RandomizeOptionsRequest request = new RandomizeOptionsRequest("nonExistentId");
        assertThrows(AssessmentOperationException.class, () -> {
            assessmentService.randomizeOptions(request);
        }, "Assessment not found with ID: nonExistentId");
    }

    // ===================== QUESTION MANAGEMENT OPERATIONS =====================

    @Test
    void testAddQuestionToAssessment_Success() {
        when(assessmentRepo.findById(assessmentId)).thenReturn(Optional.of(mockAssessment));
        when(questionService.questionExists("question1")).thenReturn(Boolean.valueOf(true));
        when(assessmentRepo.save(any(Assessment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assessmentService.addQuestionToAssessment(assessmentId, "question1");

        verify(assessmentRepo).save(argThat(a -> a.getQuestionIds().contains("question1")));
    }

    @Test
    void testRemoveQuestionFromAssessment_Success() {
        mockAssessment.getQuestionIds().add("question1");
        when(assessmentRepo.findById(assessmentId)).thenReturn(Optional.of(mockAssessment));
        when(assessmentRepo.save(any(Assessment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assessmentService.removeQuestionFromAssessment(assessmentId, "question1");

        verify(assessmentRepo).save(argThat(a -> !a.getQuestionIds().contains("question1")));
    }

    // ===================== DELEGATE TO OTHER SERVICES =====================

    @Test
    void testAutoGrade_DelegatesToGradingService() {
        AutoGradeRequest request = new AutoGradeRequest();
        GradeResponse mockResponse = new GradeResponse();
        when(gradingService.autoGrade(request)).thenReturn(mockResponse);

        GradeResponse response = assessmentService.autoGrade(request);

        assertSame(mockResponse, response);
        verify(gradingService).autoGrade(request);
    }
}