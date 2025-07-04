package assessment.manager.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoGradeRequest {
    private String assessmentId;
    private List<AnsweredQuestionRequest> answeredQuestions;
}
