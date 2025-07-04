package assessment.manager.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssessmentResponse {
    private String id;
    private String creatorId;
    private String title;
    private String description;
    private List<QuestionResponse> questions;
    private long timerDuration;
    private boolean isQuestionsRandomized;
    private boolean isOptionsRandomized;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
