package assessment.manager.data.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Question {
    @Id
    private String id;
    private String assessmentId;
    private String text;
    private List<String> optionIds;
    private String correctOptionId;
    private List<String> tags;
}
