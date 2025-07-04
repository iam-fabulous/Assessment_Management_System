package assessment.manager.opt;

import assessment.manager.data.models.User;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@Getter

@AllArgsConstructor
@NoArgsConstructor

@Builder
public class OtpModel {
    @Id
    private Long id;
    private User user;
    private String otp;
    private final LocalDateTime createdAt = LocalDateTime.now();
    private final LocalDateTime expirationTime = createdAt.plusMinutes(10L);
}
