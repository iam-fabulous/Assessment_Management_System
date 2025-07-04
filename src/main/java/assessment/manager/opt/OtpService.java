package assessment.manager.opt;

import assessment.manager.data.models.User;

public interface OtpService {
    String generateAndSaveOtp(User user);
    OtpModel validateReceivedOtp(OtpModel receivedOtp,Long userId);
    void deleteAllOtpsForUser(OtpModel userId);
}
