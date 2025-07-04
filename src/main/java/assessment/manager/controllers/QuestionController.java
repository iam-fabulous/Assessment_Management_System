package assessment.manager.controllers;

import assessment.manager.dtos.requests.CreateQuestionRequest;
import assessment.manager.dtos.requests.UpdateQuestionRequest;
import assessment.manager.dtos.responses.QuestionResponse;
import assessment.manager.services.interfaces.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * REST Controller for managing questions
 * 
 * This controller provides endpoints for:
 * - Creating new questions
 * - Updating existing questions
 * - Retrieving questions by ID
 * - Deleting questions
 * - Getting all questions
 * 
 * @author Assessment Management System
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class QuestionController {

    private final QuestionService questionService;

    /**
     * Create a new question
     */
    @PostMapping
    public ResponseEntity<QuestionResponse> createQuestion(
            @Valid @RequestBody CreateQuestionRequest request) {
        
        log.info("Creating new question for assessment: {}", request.getAssessmentId());
        
        try {
            QuestionResponse response = questionService.createQuestion(request);
            log.info("Question created successfully with ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error creating question: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update an existing question
     */
    @PutMapping("/{questionId}")
    public ResponseEntity<QuestionResponse> updateQuestion(
            @PathVariable String questionId,
            @Valid @RequestBody UpdateQuestionRequest request) {
        
        log.info("Updating question with ID: {}", questionId);
        
        try {
            // Ensure the ID in the path matches the request
            request.setQuestionId(questionId);
            QuestionResponse response = questionService.updateQuestion(request);
            log.info("Question updated successfully: {}", questionId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating question {}: {}", questionId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get a specific question by ID
     */
    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionResponse> getQuestionById(
            @PathVariable String questionId) {
        
        log.info("Fetching question with ID: {}", questionId);
        
        try {
            QuestionResponse response = questionService.getQuestionById(questionId);
            log.info("Question fetched successfully: {}", questionId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching question {}: {}", questionId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Get all questions
     */
    @GetMapping
    public ResponseEntity<List<QuestionResponse>> getAllQuestions() {
        
        log.info("Fetching all questions");
        
        try {
            List<QuestionResponse> questions = questionService.getAllQuestions();
            log.info("Found {} questions", questions.size());
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            log.error("Error fetching all questions: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete a specific question
     */
    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(
            @PathVariable String questionId) {
        
        log.info("Deleting question with ID: {}", questionId);
        
        try {
            questionService.deleteQuestion(questionId);
            log.info("Question deleted successfully: {}", questionId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting question {}: {}", questionId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}