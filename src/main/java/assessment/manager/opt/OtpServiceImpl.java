package assessment.manager.opt;

import assessment.manager.data.models.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class OtpServiceImpl implements OtpService {
    private final OtpRepository otpRepository;

    public OtpServiceImpl(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    @Override
    public String generateAndSaveOtp(User user) {
        OtpModel existingOtp = otpRepository.findOtpModelByUser(user);
        if (existingOtp != null) {
            otpRepository.delete( existingOtp );
        }
        String generatedOtp = generateOtp();
        OtpModel otpModel = OtpModel.builder()
                .user(user)
                .otp(generatedOtp)
                .build();
        otpRepository.save(otpModel);
        return generatedOtp;
    }

    private static String generateOtp() {
        SecureRandom secureRandom = new SecureRandom();
        return String.valueOf(secureRandom.nextInt(10000, 100000));
    }

    @Override
    public OtpModel validateReceivedOtp(OtpModel receivedOtp, Long userId) {
        OtpModel otpModel = otpRepository.findByOtpAndUser_Id(receivedOtp.getOtp(),userId);
        if (otpModel == null){
            throw new RuntimeException("otp is invalid");
        }else if (otpModel.getExpirationTime().isBefore( LocalDateTime.now())){

            otpRepository.delete(otpModel);
            throw  new RuntimeException("otp is expired");

        }
        return otpModel;
    }

    @Override
    public void deleteAllOtpsForUser(OtpModel otpModel) {
        otpRepository.delete(otpModel);
    }
}
