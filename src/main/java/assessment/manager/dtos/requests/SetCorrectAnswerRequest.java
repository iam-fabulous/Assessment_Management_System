package assessment.manager.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetCorrectAnswerRequest {
    private String questionId;
    private String optionId;
}
