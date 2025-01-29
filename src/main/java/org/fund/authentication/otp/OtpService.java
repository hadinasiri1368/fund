package org.fund.authentication.otp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fund.authentication.otp.constant.OtpConsumerType;
import org.fund.authentication.otp.dto.OtpResponseDto;
import org.fund.authentication.otp.generator.OtpGenerator;
import org.fund.common.FundUtils;
import org.fund.config.dataBase.TenantContext;
import org.fund.exception.AuthenticationExceptionType;
import org.fund.exception.FundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class OtpService {
    private final String key = "_otp_";
    private final RedisTemplate<String, Object> redisTemplate;

    public OtpService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Value("${verificationCode.user.attempts-number}")
    private Integer userAttemptsNumber;
    @Value("${verificationCode.customer.attempts-number}")
    private Integer customerAttemptsNumber;
    @Value("${verificationCode.user.expiration-time}")
    private Integer userExpirationTime;
    @Value("${verificationCode.customer.expiration-time}")
    private Integer customerExpirationTime;

    public void verifyCode(String identityCode, String code) {
        OtpResponseDto otp = getOtp(identityCode);
        if (FundUtils.isNull(otp)) {
            throw new FundException(AuthenticationExceptionType.VERIFY_CODE_IS_NOT_VALID);
        }
        if (!OtpGenerator.checkOtpTime(otp.getExpirationTime())) {
            removeOtp(identityCode);
            throw new FundException(AuthenticationExceptionType.VERIFY_CODE_TIMEOUT);
        }
        if (otp.getNumberOfAttempts() == 0) {
            throw new FundException(AuthenticationExceptionType.VERIFY_CODE_ATTEMPT_EXCEED);
        }
        otp.setNumberOfAttempts(otp.getNumberOfAttempts() - 1);
        setOtp(identityCode, otp);
        if (!otp.getVerificationCode().equals(code)) {
            throw new FundException(AuthenticationExceptionType.VERIFY_CODE_IS_NOT_VALID);
        }
        removeOtp(identityCode);
    }

    public OtpResponseDto checkAndGenerateOtpCode(String identityCode, OtpConsumerType type) {
        OtpResponseDto otpDto = getOtp(identityCode);
        if (!FundUtils.isNull(otpDto)) {
            return otpDto;
        } else {
            Integer expirationTime;
            Integer attemptsNumber;
            if (type.equals(OtpConsumerType.CUSTOMER)) {
                expirationTime = customerExpirationTime;
                attemptsNumber = customerAttemptsNumber;
            } else {
                expirationTime = userExpirationTime;
                attemptsNumber = userAttemptsNumber;
            }
            OtpResponseDto otp = OtpGenerator.generate(expirationTime, attemptsNumber);
            setOtp(identityCode, otp);
            return otp;
        }
    }

    public OtpResponseDto getOtp(String identityCode) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(redisTemplate.opsForValue().get(TenantContext.getCurrentTenant() + key + identityCode), OtpResponseDto.class);
    }

    public void removeOtp(String identityCode) {
        redisTemplate.delete(TenantContext.getCurrentTenant() + key + identityCode);
    }

    public void setOtp(String identityCode, OtpResponseDto otpResponseDto) {
        redisTemplate.opsForValue().set(TenantContext.getCurrentTenant() + key + identityCode, otpResponseDto);
    }

    public void updateOtpStatus(String identityCode, boolean messageSend) {
        OtpResponseDto otpDto = getOtp(identityCode);
        if (!FundUtils.isNull(otpDto)) {
            otpDto.setMessageSend(messageSend);
            setOtp(identityCode, otpDto);
        }
    }
}
