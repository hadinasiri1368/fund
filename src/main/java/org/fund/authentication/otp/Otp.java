package org.fund.authentication.otp;

import org.fund.authentication.otp.constant.OtpStrategyType;

public interface Otp {
    void verifyUserOtp(String identityCode,String otpCode);
    boolean accept(OtpStrategyType type);
    void generateAndSendUserOtp(String identityCode);
}
