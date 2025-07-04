package assessment.manager.opt.mailRequest;

public interface MailService {
    String sendMail ( String email, String subject, String htmlContent);
}
