package assessment.manager.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAssessmentRequest {
    @NotBlank(message = "Creator ID cannot be null or empty")
    private String creatorId;
    
    @NotBlank(message = "Title cannot be null or empty")
    private String title;
    
    @NotBlank(message = "Description cannot be null or empty")
    private String description;
    
    @Min(value = 1, message = "Timer duration must be positive (greater than 0 seconds)")
    private int timerDuration;
    
    private boolean randomizeOptions;
    private boolean randomizeQuestions;
}
