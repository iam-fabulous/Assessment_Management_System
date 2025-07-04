package assessment.manager.data.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Assessment {
    @Id
    private String id;
    private String creatorId;
    private String title;
    private String description;
    private List<String> questionIds;
    private long timerDuration;
    private boolean isOptionsRandomize;
    private boolean isQuestionsRandomize;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
