package assessment.manager.services.interfaces;

import assessment.manager.dtos.requests.CreateOptionRequest;
import assessment.manager.dtos.requests.UpdateOptionRequest;
import assessment.manager.dtos.responses.OptionResponse;
import assessment.manager.data.models.Option;

import java.util.List;

public interface OptionService {
    
    // Core CRUD operations
    OptionResponse createOption(CreateOptionRequest request);
    OptionResponse updateOption(UpdateOptionRequest request);
    OptionResponse getOptionById(String optionId);
    void deleteOption(String optionId);
    List<OptionResponse> getAllOptionsForQuestion(String questionId);
    
    // Internal operations (used by other services)
    Option getOptionEntityById(String optionId);
    boolean optionExists(String optionId);
    List<Option> getOptionEntitiesByIds(List<String> optionIds);
}