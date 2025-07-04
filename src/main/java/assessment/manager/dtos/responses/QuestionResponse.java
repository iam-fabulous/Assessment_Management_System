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
public class QuestionResponse {
    private String id;
    private String text;
    private List<OptionResponse> options;
    private String correctOptionId;
    private List<String> tags;
}
