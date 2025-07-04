import assessment.manager.exceptions.InvalidAssessmentRequestException;
import assessment.manager.services.implementations.AssessmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import assessment.manager.data.models.Assessment;
import assessment.manager.data.repositories.AssessmentRepo;
import assessment.manager.data.repositories.OptionRepo;
import assessment.manager.data.repositories.QuestionRepo;
import assessment.manager.dtos.requests.CreateAssessmentRequest;
import assessment.manager.dtos.responses.AssessmentResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssessmentServiceImplTest {

    @Mock
    private AssessmentRepo assessmentRepo;

    @Mock
    private QuestionRepo questionRepo;

    @Mock
    private OptionRepo optionRepo;

    @InjectMocks
    private AssessmentServiceImpl assessmentService;

    private CreateAssessmentRequest validRequest;
    private Assessment savedAssessment;

    @BeforeEach
    void setUp() {
        validRequest = new CreateAssessmentRequest(
                "creator123",
                "Test Assessment",
                "This is a test assessment",
                3600,
                true,
                false
        );

        savedAssessment = Assessment.builder()
                .id("assessment123")
                .creatorId("creator123")
                .title("Test Assessment")
                .description("This is a test assessment")
                .questionIds(new ArrayList<>())
                .timerDuration(3600)
                .isOptionsRandomize(true)
                .isQuestionsRandomize(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createAssessment_ValidRequest_ShouldReturnAssessmentResponse() {
        // Arrange
        when(assessmentRepo.save(any(Assessment.class))).thenReturn(savedAssessment);

        // Act
        AssessmentResponse response = assessmentService.createAssessment(validRequest);

        // Assert
        assertNotNull(response);
        assertEquals("assessment123", response.getId());
        assertEquals("creator123", response.getCreatorId());
        assertEquals("Test Assessment", response.getTitle());
        assertEquals("This is a test assessment", response.getDescription());
        assertEquals(3600, response.getTimerDuration());
        assertTrue(response.isOptionsRandomized());
        assertFalse(response.isQuestionsRandomized());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getUpdatedAt());
        assertTrue(response.getQuestions().isEmpty());

        verify(assessmentRepo, times(1)).save(any(Assessment.class));
    }

    @Test
    void createAssessment_NullRequest_ShouldThrowException() {
        // Act & Assert
        InvalidAssessmentRequestException exception = assertThrows(InvalidAssessmentRequestException.class, () -> {
            assessmentService.createAssessment(null);
        });
        
        assertEquals("Assessment request cannot be null", exception.getMessage());
        verify(assessmentRepo, never()).save(any(Assessment.class));
    }

    @Test
    void createAssessment_NullCreatorId_ShouldThrowException() {
        // Arrange
        CreateAssessmentRequest requestWithNullCreatorId = new CreateAssessmentRequest(
                null,
                "Test Assessment",
                "This is a test assessment",
                3600,
                true,
                false
        );

        // Act & Assert
        InvalidAssessmentRequestException exception = assertThrows(InvalidAssessmentRequestException.class, () -> {
            assessmentService.createAssessment(requestWithNullCreatorId);
        });
        
        assertEquals("Creator ID cannot be null or empty", exception.getMessage());
        verify(assessmentRepo, never()).save(any(Assessment.class));
    }

    @Test
    void createAssessment_EmptyCreatorId_ShouldThrowException() {
        // Arrange
        CreateAssessmentRequest requestWithEmptyCreatorId = new CreateAssessmentRequest(
                "   ",
                "Test Assessment",
                "This is a test assessment",
                3600,
                true,
                false
        );

        // Act & Assert
        InvalidAssessmentRequestException exception = assertThrows(InvalidAssessmentRequestException.class, () -> {
            assessmentService.createAssessment(requestWithEmptyCreatorId);
        });
        
        assertEquals("Creator ID cannot be null or empty", exception.getMessage());
        verify(assessmentRepo, never()).save(any(Assessment.class));
    }

    @Test
    void createAssessment_NullTitle_ShouldThrowException() {
        // Arrange
        CreateAssessmentRequest requestWithNullTitle = new CreateAssessmentRequest(
                "creator123",
                null,
                "This is a test assessment",
                3600,
                true,
                false
        );

        // Act & Assert
        InvalidAssessmentRequestException exception = assertThrows(InvalidAssessmentRequestException.class, () -> {
            assessmentService.createAssessment(requestWithNullTitle);
        });
        
        assertEquals("Title cannot be null or empty", exception.getMessage());
        verify(assessmentRepo, never()).save(any(Assessment.class));
    }

    @Test
    void createAssessment_EmptyTitle_ShouldThrowException() {
        // Arrange
        CreateAssessmentRequest requestWithEmptyTitle = new CreateAssessmentRequest(
                "creator123",
                "",
                "This is a test assessment",
                3600,
                true,
                false
        );

        // Act & Assert
        InvalidAssessmentRequestException exception = assertThrows(InvalidAssessmentRequestException.class, () -> {
            assessmentService.createAssessment(requestWithEmptyTitle);
        });
        
        assertEquals("Title cannot be null or empty", exception.getMessage());
        verify(assessmentRepo, never()).save(any(Assessment.class));
    }

    @Test
    void createAssessment_NullDescription_ShouldThrowException() {
        // Arrange
        CreateAssessmentRequest requestWithNullDescription = new CreateAssessmentRequest(
                "creator123",
                "Test Assessment",
                null,
                3600,
                true,
                false
        );

        // Act & Assert
        InvalidAssessmentRequestException exception = assertThrows(InvalidAssessmentRequestException.class, () -> {
            assessmentService.createAssessment(requestWithNullDescription);
        });
        
        assertEquals("Description cannot be null or empty", exception.getMessage());
        verify(assessmentRepo, never()).save(any(Assessment.class));
    }

    @Test
    void createAssessment_EmptyDescription_ShouldThrowException() {
        // Arrange
        CreateAssessmentRequest requestWithEmptyDescription = new CreateAssessmentRequest(
                "creator123",
                "Test Assessment",
                "   ",
                3600,
                true,
                false
        );

        // Act & Assert
        InvalidAssessmentRequestException exception = assertThrows(InvalidAssessmentRequestException.class, () -> {
            assessmentService.createAssessment(requestWithEmptyDescription);
        });
        
        assertEquals("Description cannot be null or empty", exception.getMessage());
        verify(assessmentRepo, never()).save(any(Assessment.class));
    }

    @Test
    void createAssessment_ZeroTimerDuration_ShouldThrowException() {
        // Arrange
        CreateAssessmentRequest requestWithZeroTimer = new CreateAssessmentRequest(
                "creator123",
                "Test Assessment",
                "This is a test assessment",
                0,
                true,
                false
        );

        // Act & Assert
        InvalidAssessmentRequestException exception = assertThrows(InvalidAssessmentRequestException.class, () -> {
            assessmentService.createAssessment(requestWithZeroTimer);
        });
        
        assertEquals("Timer duration must be positive (greater than 0 seconds)", exception.getMessage());
        verify(assessmentRepo, never()).save(any(Assessment.class));
    }

    @Test
    void createAssessment_NegativeTimerDuration_ShouldThrowException() {
        // Arrange
        CreateAssessmentRequest requestWithNegativeTimer = new CreateAssessmentRequest(
                "creator123",
                "Test Assessment",
                "This is a test assessment",
                -100,
                true,
                false
        );

        // Act & Assert
        InvalidAssessmentRequestException exception = assertThrows(InvalidAssessmentRequestException.class, () -> {
            assessmentService.createAssessment(requestWithNegativeTimer);
        });
        
        assertEquals("Timer duration must be positive (greater than 0 seconds)", exception.getMessage());
        verify(assessmentRepo, never()).save(any(Assessment.class));
    }

    @Test
    void createAssessment_ValidRequest_ShouldVerifyAssessmentResponseConversion() {
        // Arrange
        when(assessmentRepo.save(any(Assessment.class))).thenReturn(savedAssessment);

        // Act
        AssessmentResponse response = assessmentService.createAssessment(validRequest);

        // Assert
        assertNotNull(response);
        assertEquals(savedAssessment.getId(), response.getId());
        assertEquals(savedAssessment.getCreatorId(), response.getCreatorId());
        assertEquals(savedAssessment.getTitle(), response.getTitle());
        assertEquals(savedAssessment.getDescription(), response.getDescription());
        assertEquals(savedAssessment.getTimerDuration(), response.getTimerDuration());
        assertEquals(savedAssessment.isOptionsRandomize(), response.isOptionsRandomized());
        assertEquals(savedAssessment.isQuestionsRandomize(), response.isQuestionsRandomized());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getUpdatedAt());
        assertNotNull(response.getQuestions());
        assertTrue(response.getQuestions().isEmpty());

        verify(assessmentRepo, times(1)).save(any(Assessment.class));
    }


}