package assessment.manager.data.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class AnsweredQuestion {
    private String questionId;
    private String selectedOptionId;
}
