package assessment.manager.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder

public class UpdateQuestionRequest {
    private String questionId;

    public UpdateQuestionRequest() {
    }

    public UpdateQuestionRequest(String questionId, String text, List<String> tags) {
        this.questionId = questionId;
        this.text = text;
        this.tags = tags;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    private String text;
    private List<String> tags;
}