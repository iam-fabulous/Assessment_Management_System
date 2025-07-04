package assessment.manager.opt.mailRequest.mail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Getter
@Data
public class SendEmailRequest {

    @Id
    private String id;

    @JsonProperty("sender")
    @Field("sender")
    private Sender sender = new Sender("MyCompany", "info@mycompany.com");

    @JsonProperty("to")
    @Field("recipients")
    private List<Recipient> recipients = new ArrayList<>();

    @JsonProperty("subject")
    @Field("subject")
    private String subject;

    @JsonProperty("htmlContent")
    @Field("htmlContent")
    private String content;
}
