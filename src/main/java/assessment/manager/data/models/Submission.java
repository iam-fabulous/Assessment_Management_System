package assessment.manager.data.models;

import assessment.manager.enums.SubmissionStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
@Data
public class Submission {
    @Id
    private String id;
    private String participantId;
    private String assessmentId;
    private List<AnsweredQuestion> answeredQuestions;
    private Integer score;
    private LocalDateTime submittedAt;
    private LocalDateTime startedAt;
    private SubmissionStatus status;
}
