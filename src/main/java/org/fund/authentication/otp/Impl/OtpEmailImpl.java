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
public class OtpEmailImpl extends OtpAbstract {
    public OtpEmailImpl(JpaRepository repository, OtpService otpService) {
        super(repository, otpService);
    }

    @Override
    public void verifyUserOtp(String identityCode, String otpCode) {
        otpService.verifyCode(identityCode, otpCode);
    }

    @Override
    public boolean accept(OtpStrategyType type) {
        return OtpStrategyType.EMAIL.equals(type);
    }

    @Override
    public void generateAndSendUserOtp(String identityCode) {
        Users user = getUser(identityCode);
        String email = user.getPerson().getEmail();
        if (FundUtils.isNull(email) || !FundUtils.isEmailValid(email))
            throw new FundException(AuthenticationExceptionType.EMAIL_IS_NOT_VALID);
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
        send(identityCode, message, Consts.EMAIL_FROM_ADDRESS_NO_REPLY, email);
        otpService.updateOtpStatus(identityCode, true);
    }

    @Override
    public void send(String identityCode, String message, String from, String to) {
        Users user = getUser(identityCode);
        try {
            String requestTime = TimeUtils.getNowTime(TimeFormat.HOUR_MINUTE);
            String requestDate = DateUtils.getTodayJalali();
            String sql = "INSERT INTO EMAIL \n" +
                    "                        (EMAIL_ID,\n" +
                    "                        FROM_ADDRESS,\n" +
                    "                        TO_ADDRESS,\n" +
                    "                        SUBJECT,\n" +
                    "                        CONTENT,\n" +
                    "                        FILENAME,\n" +
                    "                        FILENAME_FRIENDLY,\n" +
                    "                        REQUEST_DATE,\n" +
                    "                        REQUEST_TIME,\n" +
                    "                        SEND_STATUS,\n" +
                    "                        SEND_DATE,\n" +
                    "                        SEND_TIME,\n" +
                    "                        ERROR_MESSAGE,\n" +
                    "                        IS_MANUAL)\n" +
                    "     VALUES (" +
                    "             hibernate_sequence.NEXTVAL,\n" +
                    "             :fromAddress,\n" +
                    "             :toAddress,\n" +
                    "             :subject,\n" +
                    "             :content,\n" +
                    "             null,\n" +
                    "             null,\n" +
                    "             :requestDate,\n" +
                    "             :requestTime,\n" +
                    "             0,\n" +
                    "             null,\n" +
                    "             null,\n" +
                    "             null,\n" +
                    "             1)";

            Map<String, Object> param = new HashMap<>();
            param.put("fromAddress", from);
            param.put("toAddress", to);
            param.put("subject", Consts.ALERT_EMAIL_SUBJECT_VERIFICATION_CODE);
            param.put("content", message);
            param.put("requestDate", requestDate);
            param.put("requestTime", requestTime);
            repository.nativeExecuteUpdate(sql, param, RequestContext.getUserId(), RequestContext.getUuid());
            log.info("Sending EMAIL OTP message to username: {} email: {} date: {} time: {}", identityCode, to, requestDate, requestTime);
        } catch (Exception e) {
            throw new FundException(GeneralExceptionType.UNKNOWN_ERROR);
        }
    }
}
