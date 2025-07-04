package assessment.manager.services.implementations;

import assessment.manager.data.models.User;
import assessment.manager.data.repositories.UserRepo;
import assessment.manager.dto.request.RegisterRequest;
import assessment.manager.dto.request.VerifyEmailRequest;
import assessment.manager.dto.response.OtpVerificationResponse;
import assessment.manager.enums.UserRole;
import assessment.manager.opt.mailRequest.MailService;
import assessment.manager.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
//@Autowired
//    private final MailService mailService;

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl( UserRepo userRepository, PasswordEncoder passwordEncoder) {
//        this.mailService = mailService;

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public User register(RegisterRequest registerRequest) {
//        if (adminUser.getRole() != UserRole.ADMIN) {
//            throw new RuntimeException("Only ADMIN can register new users.");
//        }

        if (registerRequest.getRole() == UserRole.ADMIN) {
            throw new RuntimeException("Cannot register another ADMIN.");
        }

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists.");
        }

        User newUser = new User();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setRole(registerRequest.getRole());

        return userRepository.save(newUser);
    }

    private void checkIfUserExistsByEmail(String email) {
       boolean isPresent = userRepository.findByEmail( email ).isPresent( );
      if(isPresent){
          User user = userRepository.findByEmail(email).get();
          if(!user.isEnabled());
          resendVerificationEmail( email );
          throw new RuntimeException("User already exists.");
      }
    }
    private void sendVerificationEmail(User user, String otp){
        String mailTemplate = AppUtilities.GET_EMAIL_VERIFICATION_MAIL_TEMPLATE;

        String email = user.getEmail();
        String subject = "Email Verification";
        String htmlContent = String.format(mailTemplate,  otp);
//        mailService.sendMail(email, subject, htmlContent);

//        String htmlContent   = String.format( mailTemplate, name ,otp) mailTemplate.replace("{name}", name).replace("{otp}", otp);

    }

    @Override
    public String resendVerificationEmail(String email) {
        return "";
    }

    @Override
    public OtpVerificationResponse verifyOtp(VerifyEmailRequest verifyEmailRequest) {
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }


}