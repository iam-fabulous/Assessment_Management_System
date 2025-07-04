package assessment.manager.services.implementations;

import assessment.manager.data.models.Option;
import assessment.manager.data.repositories.OptionRepo;
import assessment.manager.dtos.requests.CreateOptionRequest;
import assessment.manager.dtos.requests.UpdateOptionRequest;
import assessment.manager.dtos.responses.OptionResponse;
import assessment.manager.exceptions.InvalidAssessmentRequestException;
import assessment.manager.exceptions.OptionNotFoundException;
import assessment.manager.services.interfaces.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {
    
    private final OptionRepo optionRepo;
    
    @Override
    public OptionResponse createOption(CreateOptionRequest request) {
        try {
            validateCreateOptionRequest(request);
            
            Option option = Option.builder()
                    .id(null)
                    .text(request.getText())
                    .build();
            
            Option savedOption = optionRepo.save(option);
            return convertToOptionResponse(savedOption);
        } catch (Exception e) {
            throw new RuntimeException("Error creating option", e);
        }
    }
    
    @Override
    public OptionResponse updateOption(UpdateOptionRequest request) {
        try {
            validateUpdateOptionRequest(request);
            
            Option existingOption = getOptionEntityById(request.getOptionId());
            
            Option updatedOption = Option.builder()
                    .id(existingOption.getId())
                    .text(request.getText())
                    .build();
            
            Option savedOption = optionRepo.save(updatedOption);
            return convertToOptionResponse(savedOption);
        } catch (Exception e) {
            throw new RuntimeException("Error updating option: " + request.getOptionId(), e);
        }
    }
    
    @Override
    public OptionResponse getOptionById(String optionId) {
        try {
            Option option = getOptionEntityById(optionId);
            return convertToOptionResponse(option);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching option: " + optionId, e);
        }
    }
    
    @Override
    public void deleteOption(String optionId) {
        try {
            // Verify option exists
            getOptionEntityById(optionId);
            
            // Delete the option
            optionRepo.deleteById(optionId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting option: " + optionId, e);
        }
    }
    
    @Override
    public List<OptionResponse> getAllOptionsForQuestion(String questionId) {
        return new ArrayList<>();
    }
    
    @Override
    public Option getOptionEntityById(String optionId) {
        return optionRepo.findById(optionId)
                .orElseThrow(() -> new OptionNotFoundException("Option not found with ID: " + optionId));
    }
    
    @Override
    public boolean optionExists(String optionId) {
        return optionRepo.existsById(optionId);
    }
    
    @Override
    public List<Option> getOptionEntitiesByIds(List<String> optionIds) {
        return optionRepo.findAllById(optionIds);
    }
    
    private OptionResponse convertToOptionResponse(Option option) {
        return OptionResponse.builder()
                .id(option.getId())
                .text(option.getText())
                .build();
    }
    
    private void validateCreateOptionRequest(CreateOptionRequest request) {
        if (request == null) {
            throw new InvalidAssessmentRequestException("Option request cannot be null");
        }
        
        if (request.getText() == null || request.getText().trim().isEmpty()) {
            throw new InvalidAssessmentRequestException("Option text cannot be null or empty");
        }
        
        if (request.getText().length() > 500) {
            throw new InvalidAssessmentRequestException("Option text cannot exceed 500 characters");
        }
        
        if (request.getQuestionId() == null || request.getQuestionId().trim().isEmpty()) {
            throw new InvalidAssessmentRequestException("Question ID cannot be null or empty");
        }
    }
    
    private void validateUpdateOptionRequest(UpdateOptionRequest request) {
        if (request == null) {
            throw new InvalidAssessmentRequestException("Option update request cannot be null");
        }
        
        if (request.getOptionId() == null || request.getOptionId().trim().isEmpty()) {
            throw new InvalidAssessmentRequestException("Option ID cannot be null or empty");
        }
        
        if (request.getText() == null || request.getText().trim().isEmpty()) {
            throw new InvalidAssessmentRequestException("Option text cannot be null or empty");
        }
        
        if (request.getText().length() > 500) {
            throw new InvalidAssessmentRequestException("Option text cannot exceed 500 characters");
        }
    }
}