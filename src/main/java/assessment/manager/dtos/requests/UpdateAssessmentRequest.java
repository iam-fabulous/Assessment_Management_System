package assessment.manager.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAssessmentRequest {
    private String assessmentId;
    private String title;
    private String description;
    private long timerDuration;
    private boolean randomizeOptions;
    private boolean randomizeQuestions;
}