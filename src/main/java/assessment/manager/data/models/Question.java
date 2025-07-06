package assessment.manager.data.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Question {
    @Id
    private String id;
    private String assessmentId;
    private String text;
    private List<String> optionIds;
    private String correctOptionId;
    private List<String> tags;

//    public Question() {
//    }
//
//    public Question(String id, String assessmentId, String text, List<String> optionIds, String correctOptionId, List<String> tags) {
//        this.id = id;
//        this.assessmentId = assessmentId;
//        this.text = text;
//        this.optionIds = optionIds;
//        this.correctOptionId = correctOptionId;
//        this.tags = tags;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptionIds() {
        return optionIds;
    }

    public void setOptionIds(List<String> optionIds) {
        this.optionIds = optionIds;
    }

    public String getCorrectOptionId() {
        return correctOptionId;
    }

    public void setCorrectOptionId(String correctOptionId) {
        this.correctOptionId = correctOptionId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
