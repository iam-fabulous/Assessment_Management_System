package assessment.manager.data.models;

import assessment.manager.enums.UserRole;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
@Data
@Getter
public class User {
    @Id
    private String id;
    private String email;
    private String password;
    private UserRole role;
    private boolean isEnabled;
}
