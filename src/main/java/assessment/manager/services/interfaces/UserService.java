package assessment.manager.services.interfaces;

import assessment.manager.data.models.User;
import assessment.manager.dto.request.RegisterRequest;
import assessment.manager.dto.request.VerifyEmailRequest;
import assessment.manager.dto.response.OtpVerificationResponse;

public interface UserService {
    User register(RegisterRequest registerRequest);
    String  resendVerificationEmail(String email);
    OtpVerificationResponse verifyOtp(VerifyEmailRequest verifyEmailRequest);
    User getUserByEmail(String email);

}
