package org.fund.authentication.otp.Impl;

import lombok.extern.slf4j.Slf4j;
import org.fund.authentication.otp.*;
import org.fund.authentication.otp.constant.OtpConsumerType;
import org.fund.authentication.otp.constant.OtpStrategyType;
import org.fund.authentication.otp.dto.OtpResponseDto;
import org.fund.authentication.otp.generator.OtpGenerator;
import org.fund.common.DateUtils;
import org.fund.common.FundUtils;
import org.fund.common.TimeUtils;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.constant.TimeFormat;
import org.fund.exception.AuthenticationExceptionType;
import org.fund.exception.FundException;
import org.fund.exception.GeneralExceptionType;
import org.fund.model.Company;
import org.fund.model.Users;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class OtpSmsImpl extends OtpAbstract {
    public OtpSmsImpl(JpaRepository repository, OtpService otpService) {
        super(repository, otpService);
    }

    @Override
    public void verifyUserOtp(String identityCode, String otpCode) {
        otpService.verifyCode(identityCode, otpCode);
    }

    @Override
    public boolean accept(OtpStrategyType type) {
        return OtpStrategyType.SMS.equals(type);
    }

    @Override
    public void generateAndSendUserOtp(String identityCode) {
        Users user = getUser(identityCode);
        String mobile = user.getPerson().getCellPhone();
        if (FundUtils.isNull(mobile) || !FundUtils.isMobileValid(mobile))
            throw new FundException(AuthenticationExceptionType.USER_MOBILE_IS_NOT_VALID);
        OtpResponseDto otpDto = otpService.checkAndGenerateOtpCode(identityCode, OtpConsumerType.USER);
        if (OtpGenerator.checkOtpTime(otpDto.getExpirationTime())) {
            if (otpDto.getMessageSend()) {
                throw new FundException(AuthenticationExceptionType.OTP_SEND_NOTALLOWED);
            }
        } else {
            otpService.removeOtp(identityCode);
            otpDto = otpService.checkAndGenerateOtpCode(identityCode, OtpConsumerType.USER);
        }
        Company company = repository.findAll(Company.class).stream().findFirst().get();
        String message = FundUtils.getMessage("authorization.otp_message", new Object[]{otpDto.getVerificationCode()});
        message += "\n" + company.getName();
        send(identityCode, message, company.getSmsInto(), mobile);
        otpService.updateOtpStatus(identityCode, true);
    }

    @Override
    public void send(String identityCode, String message, String from, String to) {
        Users user = getUser(identityCode);
        try {
            String requestTime = TimeUtils.getNowTime(TimeFormat.HOUR_MINUTE);
            String requestDate = DateUtils.getTodayJalali();
            String sql = "INSERT INTO sms" +
                    "        (sms_id, from_number, TO_NUMBER, content, request_date, request_time, send_status,sms_type)" +
                    " VALUES (hibernate_sequence.nextval, :fromNumber, :toNumber, :content,:requestDate,:requestTime,0,:smsType)";
            Map<String, Object> param = new HashMap<>();
            param.put("fromNumber", from);
            param.put("toNumber", to);
            param.put("content", message);
            param.put("requestDate", requestDate);
            param.put("requestTime", requestTime);
            param.put("smsType", Consts.SMS_TYPE_HIGH_PRIORITY_SENDING);
            repository.nativeExecuteUpdate(sql, param, RequestContext.getUserId(), RequestContext.getUuid());
            log.info("Sending SMS OTP message to username: {} mobile: {} date: {} time: {}", identityCode, to, requestDate, requestTime);
        } catch (Exception e) {
            throw new FundException(GeneralExceptionType.UNKNOWN_ERROR);
        }
    }
}
