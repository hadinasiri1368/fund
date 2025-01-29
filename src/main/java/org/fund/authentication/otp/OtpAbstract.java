package org.fund.authentication.otp;

import org.fund.authentication.otp.constant.OtpStrategyType;
import org.fund.exception.AuthenticationExceptionType;
import org.fund.exception.FundException;
import org.fund.model.Users;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public abstract class OtpAbstract implements Otp {
    protected final JpaRepository repository;
    protected final OtpService otpService;
    public OtpAbstract(final JpaRepository repository, OtpService otpService) {
        this.repository = repository;
        this.otpService = otpService;
    }

    public abstract void verifyUserOtp(String identityCode, String otpCode);
    public abstract boolean accept(OtpStrategyType type);
    public abstract void generateAndSendUserOtp(String identityCode);
    public abstract void send(String identityCode, String message, String from, String to);
    protected Users getUser(String identityCode) {
        return repository.findAll(Users.class).stream()
                .filter(a -> a.getUsername().equals(identityCode))
                .findFirst()
                .orElseThrow(() -> new FundException(AuthenticationExceptionType.USER_NOT_FOUND));
    }
}
