package assessment.manager.controllers;

import assessment.manager.dtos.requests.CreateOptionRequest;
import assessment.manager.dtos.requests.UpdateOptionRequest;
import assessment.manager.dtos.responses.OptionResponse;
import assessment.manager.services.interfaces.OptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * REST Controller for managing options
 * 
 * This controller provides endpoints for:
 * - Creating new options
 * - Updating existing options
 * - Retrieving options by ID
 * - Deleting options
 * - Getting all options for a question
 * 
 * @author Assessment Management System
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/options")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class OptionController {

    private final OptionService optionService;

    /**
     * Create a new option
     */
    @PostMapping
    public ResponseEntity<OptionResponse> createOption(
            @Valid @RequestBody CreateOptionRequest request) {
        
        log.info("Creating new option for question: {}", request.getQuestionId());
        
        try {
            OptionResponse response = optionService.createOption(request);
            log.info("Option created successfully with ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error creating option: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update an existing option
     */
    @PutMapping("/{optionId}")
    public ResponseEntity<OptionResponse> updateOption(
            @PathVariable String optionId,
            @Valid @RequestBody UpdateOptionRequest request) {
        
        log.info("Updating option with ID: {}", optionId);
        
        try {
            // Ensure the ID in the path matches the request
            request.setOptionId(optionId);
            OptionResponse response = optionService.updateOption(request);
            log.info("Option updated successfully: {}", optionId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating option {}: {}", optionId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get a specific option by ID
     */
    @GetMapping("/{optionId}")
    public ResponseEntity<OptionResponse> getOptionById(
            @PathVariable String optionId) {
        
        log.info("Fetching option with ID: {}", optionId);
        
        try {
            OptionResponse response = optionService.getOptionById(optionId);
            log.info("Option fetched successfully: {}", optionId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching option {}: {}", optionId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Get all options for a specific question
     */
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<OptionResponse>> getAllOptionsForQuestion(
            @PathVariable String questionId) {
        
        log.info("Fetching all options for question: {}", questionId);
        
        try {
            List<OptionResponse> options = optionService.getAllOptionsForQuestion(questionId);
            log.info("Found {} options for question: {}", options.size(), questionId);
            return ResponseEntity.ok(options);
        } catch (Exception e) {
            log.error("Error fetching options for question {}: {}", questionId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete a specific option
     */
    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> deleteOption(
            @PathVariable String optionId) {
        
        log.info("Deleting option with ID: {}", optionId);
        
        try {
            optionService.deleteOption(optionId);
            log.info("Option deleted successfully: {}", optionId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting option {}: {}", optionId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}