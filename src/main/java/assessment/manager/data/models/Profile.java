package assessment.manager.data.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
@Document
@Data
public class Profile {
    private String userId;
    private String bio;
    private String phoneNumber;
    private String gender;
    private String profileImageUrl;
    private LocalDate dateOfBirth;
    private List<String> qualifications;
    private List<String> Experiences;
}
