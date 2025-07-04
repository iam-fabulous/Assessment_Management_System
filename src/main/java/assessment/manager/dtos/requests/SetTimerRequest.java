package assessment.manager.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetTimerRequest {
    private String assessmentId;
    private long durationMinutes;
}
