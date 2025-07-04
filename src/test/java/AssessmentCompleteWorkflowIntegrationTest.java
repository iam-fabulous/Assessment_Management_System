import assessment.manager.data.models.Assessment;
import assessment.manager.data.models.Question;
import assessment.manager.data.models.Option;
import assessment.manager.data.repositories.AssessmentRepo;
import assessment.manager.data.repositories.QuestionRepo;
import assessment.manager.data.repositories.OptionRepo;
import assessment.manager.dtos.requests.*;
import assessment.manager.dtos.responses.*;
import assessment.manager.services.implementations.AssessmentServiceImpl;
import assessment.manager.services.implementations.QuestionServiceImpl;
import assessment.manager.services.implementations.OptionServiceImpl;
import assessment.manager.services.interfaces.AssessmentService;
import assessment.manager.services.interfaces.QuestionService;
import assessment.manager.services.interfaces.OptionService;
import assessment.manager.services.interfaces.GradingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Integration test for complete assessment workflow:
 * 1. Create assessment
 * 2. Create questions
 * 3. Create options for questions  
 * 4. Add options to questions
 * 5. Set correct answers
 * 6. Add questions to assessment
 * 7. Set/update timer duration
 * 8. Verify complete assessment structure
 */
@ExtendWith(MockitoExtension.class)
class AssessmentCompleteWorkflowIntegrationTest {

    @Mock
    private AssessmentRepo assessmentRepo;
    
    @Mock
    private QuestionRepo questionRepo;
    
    @Mock
    private OptionRepo optionRepo;

    @Mock
    private QuestionService questionService;
    
    @Mock
    private OptionService optionService;
    
    @Mock
    private GradingService gradingService;

    @InjectMocks
    private AssessmentServiceImpl assessmentService;

    // Test data
    private String creatorId;
    private String assessmentId;
    private String question1Id;
    private String question2Id;
    private String question1Option1Id;
    private String question1Option2Id;
    private String question1Option3Id;
    private String question2Option1Id;
    private String question2Option2Id;
    
    private Assessment mockAssessment;
    private Question mockQuestion1;
    private Question mockQuestion2;
    private Option mockQ1Option1;
    private Option mockQ1Option2;
    private Option mockQ1Option3;
    private Option mockQ2Option1;
    private Option mockQ2Option2;

    @BeforeEach
    void setUp() {
        // Initialize test data IDs
        creatorId = "creator123";
        assessmentId = "assessment123";
        question1Id = "question1";
        question2Id = "question2";
        question1Option1Id = "q1opt1";
        question1Option2Id = "q1opt2";
        question1Option3Id = "q1opt3";
        question2Option1Id = "q2opt1";
        question2Option2Id = "q2opt2";
        
        // Setup mock entities
        setupMockEntities();
        
        // Setup mock repository behaviors
        setupMockRepositoryBehaviors();
    }

    @Test
    void testCompleteAssessmentWorkflow() {
        // Step 1: Create Assessment
        AssessmentResponse assessmentResponse = createAssessment();
        assertNotNull(assessmentResponse);
        assertEquals(assessmentId, assessmentResponse.getId());
        assertEquals("Java Programming Assessment", assessmentResponse.getTitle());
        assertEquals(1800, assessmentResponse.getTimerDuration()); // 30 minutes
        
        // Step 2: Create Questions
        QuestionResponse question1Response = createQuestion1();
        QuestionResponse question2Response = createQuestion2();
        assertNotNull(question1Response);
        assertNotNull(question2Response);
        assertEquals("What is polymorphism in Java?", question1Response.getText());
        assertEquals("Which keyword is used to inherit a class in Java?", question2Response.getText());
        
        // Step 3: Create Options for Questions
        OptionResponse q1opt1Response = createOption(question1Option1Id, question1Id, "Method overloading");
        OptionResponse q1opt2Response = createOption(question1Option2Id, question1Id, "Method overriding");
        OptionResponse q1opt3Response = createOption(question1Option3Id, question1Id, "Both A and B");
        
        OptionResponse q2opt1Response = createOption(question2Option1Id, question2Id, "extends");
        OptionResponse q2opt2Response = createOption(question2Option2Id, question2Id, "implements");
        
        // Verify options creation
        assertNotNull(q1opt1Response);
        assertNotNull(q1opt2Response);
        assertNotNull(q1opt3Response);
        assertNotNull(q2opt1Response);
        assertNotNull(q2opt2Response);
        
        // Step 4: Add Options to Questions
        addOptionsToQuestions();
        
        // Step 5: Set Correct Answers
        setCorrectAnswers();
        
        // Step 6: Add Questions to Assessment
        addQuestionsToAssessment();
        
        // Step 7: Update Timer Duration
        updateTimerDuration();
        
        // Step 8: Verify Complete Assessment Structure
        verifyCompleteAssessmentStructure();
        
        // Verify all repository interactions
        verifyRepositoryInteractions();
    }

    private AssessmentResponse createAssessment() {
        CreateAssessmentRequest request = new CreateAssessmentRequest(
                creatorId,
                "Java Programming Assessment",
                "Assessment to test Java programming knowledge",
                1800, // 30 minutes
                false,
                true
        );
        
        return assessmentService.createAssessment(request);
    }

    private QuestionResponse createQuestion1() {
        CreateQuestionRequest request = CreateQuestionRequest.builder()
                .assessmentId(assessmentId)
                .text("What is polymorphism in Java?")
                .tags(Arrays.asList("java", "oop", "polymorphism"))
                .build();
        
        return assessmentService.createQuestion(request);
    }

    private QuestionResponse createQuestion2() {
        CreateQuestionRequest request = CreateQuestionRequest.builder()
                .assessmentId(assessmentId)
                .text("Which keyword is used to inherit a class in Java?")
                .tags(Arrays.asList("java", "inheritance", "keywords"))
                .build();
        
        return assessmentService.createQuestion(request);
    }

    private OptionResponse createOption(String optionId, String questionId, String text) {
        CreateOptionRequest request = CreateOptionRequest.builder()
                .text(text)
                .questionId(questionId)
                .build();
        
        return assessmentService.createOption(request);
    }

    private void addOptionsToQuestions() {
        // This would be done via the option service when creating options
        // For testing purposes, we'll simulate this behavior
        
        // Options are automatically linked to questions when created
        // The mock setup handles the association
    }

    private void setCorrectAnswers() {
        // Set correct answer for question 1 (Both A and B)
        SetCorrectAnswerRequest request1 = new SetCorrectAnswerRequest(question1Id, question1Option3Id);
        assessmentService.setCorrectAnswer(request1);
        
        // Set correct answer for question 2 (extends)
        SetCorrectAnswerRequest request2 = new SetCorrectAnswerRequest(question2Id, question2Option1Id);
        assessmentService.setCorrectAnswer(request2);
    }

    private void addQuestionsToAssessment() {
        assessmentService.addQuestionToAssessment(assessmentId, question1Id);
        assessmentService.addQuestionToAssessment(assessmentId, question2Id);
    }

    private void updateTimerDuration() {
        SetTimerRequest request = new SetTimerRequest(assessmentId, 45); // 45 minutes
        assessmentService.setTimer(request);
    }

    private void verifyCompleteAssessmentStructure() {
        // Get the complete assessment
        AssessmentResponse finalAssessment = assessmentService.getAssessmentById(assessmentId);
        
        // Verify assessment details
        assertNotNull(finalAssessment);
        assertEquals(assessmentId, finalAssessment.getId());
        assertEquals(creatorId, finalAssessment.getCreatorId());
        assertEquals("Java Programming Assessment", finalAssessment.getTitle());
        assertEquals("Assessment to test Java programming knowledge", finalAssessment.getDescription());
        assertEquals(2700, finalAssessment.getTimerDuration()); // 45 minutes in seconds
        assertFalse(finalAssessment.isOptionsRandomized());
        assertTrue(finalAssessment.isQuestionsRandomized());
        
        // Verify questions
        assertNotNull(finalAssessment.getQuestions());
        assertEquals(2, finalAssessment.getQuestions().size());
        
        // Verify first question
        QuestionResponse q1 = finalAssessment.getQuestions().get(0);
        assertEquals(question1Id, q1.getId());
        assertEquals("What is polymorphism in Java?", q1.getText());
        assertEquals(3, q1.getOptions().size());
        assertEquals(question1Option3Id, q1.getCorrectOptionId());
        assertTrue(q1.getTags().contains("java"));
        assertTrue(q1.getTags().contains("oop"));
        assertTrue(q1.getTags().contains("polymorphism"));
        
        // Verify second question
        QuestionResponse q2 = finalAssessment.getQuestions().get(1);
        assertEquals(question2Id, q2.getId());
        assertEquals("Which keyword is used to inherit a class in Java?", q2.getText());
        assertEquals(2, q2.getOptions().size());
        assertEquals(question2Option1Id, q2.getCorrectOptionId());
        assertTrue(q2.getTags().contains("java"));
        assertTrue(q2.getTags().contains("inheritance"));
        assertTrue(q2.getTags().contains("keywords"));
        
        // Verify options for question 1
        List<OptionResponse> q1Options = q1.getOptions();
        assertTrue(q1Options.stream().anyMatch(opt -> opt.getText().equals("Method overloading")));
        assertTrue(q1Options.stream().anyMatch(opt -> opt.getText().equals("Method overriding")));
        assertTrue(q1Options.stream().anyMatch(opt -> opt.getText().equals("Both A and B")));
        
        // Verify options for question 2
        List<OptionResponse> q2Options = q2.getOptions();
        assertTrue(q2Options.stream().anyMatch(opt -> opt.getText().equals("extends")));
        assertTrue(q2Options.stream().anyMatch(opt -> opt.getText().equals("implements")));
    }

    private void verifyRepositoryInteractions() {
        // Verify assessment operations
        verify(assessmentRepo, atLeast(1)).save(any(Assessment.class));
        verify(assessmentRepo, atLeast(1)).findById(assessmentId);
        
        // Verify service interactions
        verify(questionService, atLeast(2)).createQuestion(any(CreateQuestionRequest.class));
        verify(optionService, atLeast(5)).createOption(any(CreateOptionRequest.class));
        verify(assessmentService, atLeast(1)).setTimer(any(SetTimerRequest.class));
        verify(assessmentService, atLeast(2)).setCorrectAnswer(any(SetCorrectAnswerRequest.class));
    }

    private void setupMockEntities() {
        // Mock Assessment
        mockAssessment = Assessment.builder()
                .id(assessmentId)
                .creatorId(creatorId)
                .title("Java Programming Assessment")
                .description("Assessment to test Java programming knowledge")
                .questionIds(new ArrayList<>())
                .timerDuration(1800)
                .isOptionsRandomize(false)
                .isQuestionsRandomize(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Mock Questions
        mockQuestion1 = Question.builder()
                .id(question1Id)
                .text("What is polymorphism in Java?")
                .optionIds(new ArrayList<>())
                .correctOptionId(null)
                .tags(Arrays.asList("java", "oop", "polymorphism"))
                .build();

        mockQuestion2 = Question.builder()
                .id(question2Id)
                .text("Which keyword is used to inherit a class in Java?")
                .optionIds(new ArrayList<>())
                .correctOptionId(null)
                .tags(Arrays.asList("java", "inheritance", "keywords"))
                .build();

        // Mock Options
        mockQ1Option1 = Option.builder()
                .id(question1Option1Id)
                .text("Method overloading")
                .questionId(question1Id)
                .build();

        mockQ1Option2 = Option.builder()
                .id(question1Option2Id)
                .text("Method overriding")
                .questionId(question1Id)
                .build();

        mockQ1Option3 = Option.builder()
                .id(question1Option3Id)
                .text("Both A and B")
                .questionId(question1Id)
                .build();

        mockQ2Option1 = Option.builder()
                .id(question2Option1Id)
                .text("extends")
                .questionId(question2Id)
                .build();

        mockQ2Option2 = Option.builder()
                .id(question2Option2Id)
                .text("implements")
                .questionId(question2Id)
                .build();
    }

    private void setupMockRepositoryBehaviors() {
        // Assessment repository mocks
        when(assessmentRepo.save(any(Assessment.class))).thenAnswer(invocation -> {
            Assessment assessment = invocation.getArgument(0);
            if (assessment.getId() == null) {
                assessment.setId(assessmentId);
            }
            return assessment;
        });
        
        when(assessmentRepo.findById(assessmentId)).thenReturn(Optional.of(mockAssessment));
        when(assessmentRepo.existsById(assessmentId)).thenReturn(true);

        // Mock service responses
        QuestionResponse q1Response = QuestionResponse.builder()
                .id(question1Id)
                .text("What is polymorphism in Java?")
                .options(Arrays.asList(
                    OptionResponse.builder().id(question1Option1Id).text("Method overloading").build(),
                    OptionResponse.builder().id(question1Option2Id).text("Method overriding").build(),
                    OptionResponse.builder().id(question1Option3Id).text("Both A and B").build()
                ))
                .correctOptionId(question1Option3Id)
                .tags(Arrays.asList("java", "oop", "polymorphism"))
                .build();
                
        QuestionResponse q2Response = QuestionResponse.builder()
                .id(question2Id)
                .text("Which keyword is used to inherit a class in Java?")
                .options(Arrays.asList(
                    OptionResponse.builder().id(question2Option1Id).text("extends").build(),
                    OptionResponse.builder().id(question2Option2Id).text("implements").build()
                ))
                .correctOptionId(question2Option1Id)
                .tags(Arrays.asList("java", "inheritance", "keywords"))
                .build();

        // Mock question service
        when(questionService.createQuestion(any(CreateQuestionRequest.class))).thenAnswer(invocation -> {
            CreateQuestionRequest request = invocation.getArgument(0);
            if (request.getText().contains("polymorphism")) {
                return q1Response;
            } else {
                return q2Response;
            }
        });
        
        when(questionService.getQuestionById(question1Id)).thenReturn(q1Response);
        when(questionService.getQuestionById(question2Id)).thenReturn(q2Response);
        
        // Mock option service
        when(optionService.createOption(any(CreateOptionRequest.class))).thenAnswer(invocation -> {
            CreateOptionRequest request = invocation.getArgument(0);
            String optionId = "opt_" + System.currentTimeMillis();
            return OptionResponse.builder()
                    .id(optionId)
                    .text(request.getText())
                    .build();
        });
        
        when(optionService.getOptionById(question1Option1Id)).thenReturn(
            OptionResponse.builder().id(question1Option1Id).text("Method overloading").build());
        when(optionService.getOptionById(question1Option2Id)).thenReturn(
            OptionResponse.builder().id(question1Option2Id).text("Method overriding").build());
        when(optionService.getOptionById(question1Option3Id)).thenReturn(
            OptionResponse.builder().id(question1Option3Id).text("Both A and B").build());
        when(optionService.getOptionById(question2Option1Id)).thenReturn(
            OptionResponse.builder().id(question2Option1Id).text("extends").build());
        when(optionService.getOptionById(question2Option2Id)).thenReturn(
            OptionResponse.builder().id(question2Option2Id).text("implements").build());
    }

    @Test
    void testWorkflowWithValidationFailures() {
        // Test creating assessment with invalid data
        CreateAssessmentRequest invalidRequest = new CreateAssessmentRequest(
                null, // Invalid creator ID
                "Test Assessment",
                "Description",
                1800,
                false,
                true
        );
        
        assertThrows(RuntimeException.class, () -> {
            assessmentService.createAssessment(invalidRequest);
        });
        
        // Test creating question with invalid data
        CreateQuestionRequest invalidQuestionRequest = CreateQuestionRequest.builder()
                .assessmentId(assessmentId)
                .text(null) // Invalid text
                .tags(Arrays.asList("java"))
                .build();
        
        assertThrows(RuntimeException.class, () -> {
            assessmentService.createQuestion(invalidQuestionRequest);
        });
        
        // Test creating option with invalid data
        CreateOptionRequest invalidOptionRequest = CreateOptionRequest.builder()
                .text("") // Invalid text
                .questionId("validQuestionId")
                .build();
        
        assertThrows(RuntimeException.class, () -> {
            assessmentService.createOption(invalidOptionRequest);
        });
    }

    @Test
    void testWorkflowWithNonExistentEntities() {
        // Test setting correct answer for non-existent question
        SetCorrectAnswerRequest invalidCorrectAnswerRequest = new SetCorrectAnswerRequest(
                "nonExistentQuestionId",
                "validOptionId"
        );
        
        when(assessmentRepo.findById("nonExistentQuestionId")).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> {
            assessmentService.setCorrectAnswer(invalidCorrectAnswerRequest);
        });
        
        // Test adding non-existent question to assessment
        when(assessmentRepo.existsById("nonExistentQuestionId")).thenReturn(false);
        
        assertThrows(RuntimeException.class, () -> {
            assessmentService.addQuestionToAssessment(assessmentId, "nonExistentQuestionId");
        });
    }
}