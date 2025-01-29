package org.fund.authentication.otp;

import org.fund.authentication.otp.constant.OtpStrategyType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OtpVisitor {
    private final List<Otp> otps;

    public OtpVisitor(List<Otp> otps) {
        this.otps = otps;
    }

    @Transactional
    public void verifyAppuserOtp(OtpStrategyType otpStrategyType, String identityCode, String otpCode) {
        for (Otp otp : otps) {
            if (otp.accept(otpStrategyType)) {
                otp.verifyUserOtp(identityCode, otpCode);
                return;
            }
        }
    }

    @Transactional
    public void generateAndSendUserOtp(OtpStrategyType otpStrategyType, String identityCode) {
        for (Otp otp : otps) {
            if (otp.accept(otpStrategyType)) {
                otp.generateAndSendUserOtp(identityCode);
                return;
            }
        }
    }
}
