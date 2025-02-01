package org.fund.authentication.otp.Impl;

import lombok.extern.slf4j.Slf4j;
import org.fund.authentication.otp.*;
import org.fund.authentication.otp.constant.OtpStrategyType;
import org.fund.authentication.otp.generator.OtpPhysicalGenerator;
import org.fund.common.FundUtils;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.exception.AuthenticationExceptionType;
import org.fund.exception.FundException;
import org.fund.exception.GeneralExceptionType;
import org.fund.model.Users;
import org.fund.model.VerificationCode;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OtpPhysicalImpl extends OtpAbstract {
    public OtpPhysicalImpl(JpaRepository repository, OtpService otpService) {
        super(repository, otpService);
    }

    @Override
    public void verifyUserOtp(String identityCode, String otpCode) {
        Users user = getUser(identityCode);
        if (FundUtils.isNull(user.getVerificationCodeId()))
            throw new FundException(AuthenticationExceptionType.PHYSICAL_OTP_NOT_FOUND);

        int seedCounterCheck = Consts.SEED_COUNTER;
        VerificationCode verificationCode = repository.findOne(VerificationCode.class, user.getVerificationCodeId());
        if (!verificationCode.getIsActive())
            throw new FundException(AuthenticationExceptionType.PHYSICAL_OTP_IS_DISABLED);
        if (FundUtils.isNull(verificationCode.getSeed()))
            throw new FundException(AuthenticationExceptionType.PHYSICAL_OTP_NOT_FOUND);


        String seed = verificationCode.getSeed();
        int counter = verificationCode.getCounter().intValue();
        OtpPhysicalGenerator otpHandler = new OtpPhysicalGenerator(seed, seedCounterCheck);
        try {
            int newCounter = otpHandler.validate(counter, otpCode.trim());
            verificationCode.setCounter(FundUtils.longValue(newCounter) + 1);
            repository.update(verificationCode, RequestContext.getUserId(), RequestContext.getUuid());
        } catch (Exception e) {
            log.info("OtpPhysicalImpl verifyAppuserOtp error : {}", e.getMessage());
            throw new FundException(AuthenticationExceptionType.VERIFY_CODE_IS_NOT_VALID);
        }


    }

    @Override
    public boolean accept(OtpStrategyType type) {
        return OtpStrategyType.PHYSICAL.equals(type);
    }

    @Override
    public void generateAndSendUserOtp(String identityCode) {

    }

    @Override
    public void send(String identityCode, String message, String from, String to) {

    }
}
