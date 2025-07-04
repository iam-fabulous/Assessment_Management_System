package assessment.manager.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeResponse {
    private int grade;
    private int totalQuestions;
    private int correctAnswers;
}
