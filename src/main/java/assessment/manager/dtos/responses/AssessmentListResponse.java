package assessment.manager.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssessmentListResponse {
    private List<AssessmentResponse> assessments;
    private int totalCount;
}
