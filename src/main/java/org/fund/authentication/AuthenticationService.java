package org.fund.authentication;

import org.fund.authentication.otp.constant.OtpStrategyType;
import org.fund.authentication.otp.OtpVisitor;
import org.fund.authentication.otp.dto.OtpRequestDto;
import org.fund.common.FundUtils;
import org.fund.common.JwtUtil;
import org.fund.config.authentication.TokenService;
import org.fund.constant.Consts;
import org.fund.constant.TimeFormat;
import org.fund.exception.AuthenticationExceptionType;
import org.fund.exception.FundException;
import org.fund.model.Users;
import org.fund.administration.params.ParamService;
import org.fund.repository.JpaRepository;
import org.fund.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationService {
    private final JpaRepository repository;
    private final TokenService tokenService;
    private final ProfileService profileService;
    private final ParamService paramService;
    private final OtpVisitor otpVisitor;
    private final JwtUtil jwtUtil;

    public AuthenticationService(JpaRepository repository
            , TokenService tokenService
            , ProfileService profileService
            , ParamService paramService
            , OtpVisitor otpVisitor, JwtUtil jwtUtil) {
        this.repository = repository;
        this.tokenService = tokenService;
        this.profileService = profileService;
        this.paramService = paramService;
        this.otpVisitor = otpVisitor;
        this.jwtUtil = jwtUtil;
    }

    public String login(LoginDto loginDto) throws Exception {
        Users user = getUser(loginDto.getUsername(), loginDto.getPassword());
        if (!profileService.isDebugMode()) {
            if (!getOtpStrategyTypeList().isEmpty()) {
                if (FundUtils.isNull(loginDto.getOtpStrategyTypeId()))
                    throw new FundException(AuthenticationExceptionType.VERIFY_CODE_IS_NOT_VALID);
                OtpStrategyType otpStrategyType = OtpStrategyType.getItemById(loginDto.getOtpStrategyTypeId());
                checkOtpStrategyType(otpStrategyType);
                otpVisitor.verifyAppuserOtp(otpStrategyType
                        , user.getUsername()
                        , loginDto.getOtpCode());
            }
        }
        return tokenService.generateToken(user);
    }

    public String refreshToken(String tenantId, String token) throws Exception {
        Users user = JwtUtil.getTokenData(token);
        logout(tenantId, token);
        return tokenService.generateToken(user);
    }

    public void logout(String tenantId, String token) throws Exception {
        if (FundUtils.isNull(token))
            throw new FundException(AuthenticationExceptionType.TOKEN_IS_NULL);
        tokenService.removeTokenById(tenantId, JwtUtil.getTokenData(token).getId(), token);
    }

    public List<OtpStrategyType> getOtpStrategyTypeList() {
        List<OtpStrategyType> twoFactorTypes = new ArrayList<>();
        if (paramService.getBooleanValue(Consts.PARAMS_TWO_FACTOR_LOGIN_WITH_SMS_APPUSERS)) {
            twoFactorTypes.add(OtpStrategyType.SMS);
        }
        if (paramService.getBooleanValue(Consts.PARAMS_TWO_FACTOR_LOGIN_WITH_EMAIL_APPUSERS)) {
            twoFactorTypes.add(OtpStrategyType.EMAIL);
        }
        if (paramService.getBooleanValue(Consts.PARAMS_TWO_FACTOR_LOGIN_WITH_PHYSICAL_APPUSERS)) {
            twoFactorTypes.add(OtpStrategyType.PHYSICAL);
        }
        return twoFactorTypes;
    }

    public void sendOtpForLogin(OtpRequestDto otpRequestDto) {
        if (profileService.isDebugMode())
            return;
        if (getOtpStrategyTypeList().isEmpty())
            throw new FundException(AuthenticationExceptionType.ALL_OTP_STRATEGY_ARE_DISABLED);
        Users user = getUser(otpRequestDto.getUsername(), otpRequestDto.getPassword());
        OtpStrategyType otpStrategyType = OtpStrategyType.getItemById(otpRequestDto.getOtpStrategyTypeId());
        checkOtpStrategyType(otpStrategyType);
        otpVisitor.generateAndSendUserOtp(otpStrategyType, user.getUsername());
    }

    private Users getUser(String username, String password) {
        Optional<Users> user = repository.findAll(Users.class).stream()
                .filter(a -> a.getUsername().equals(username))
                .findFirst();
        if (!user.isPresent()) {
            throw new FundException(AuthenticationExceptionType.USERNAME_PASSWORD_INVALID);
        }
        if (!profileService.isDebugMode()) {
            boolean validated = FundUtils.encodePassword(password)
                    .equalsIgnoreCase(user.get().getPassword());
            if (!validated) {
                throw new FundException(AuthenticationExceptionType.USERNAME_PASSWORD_INVALID);
            }
            if (!user.get().getIsActive()) {
                throw new FundException(AuthenticationExceptionType.USER_IS_NOT_ACTIVE);
            }
        }
        return user.get();
    }

    private void checkOtpStrategyType(OtpStrategyType otpStrategyType) {
        if (!getOtpStrategyTypeList().contains(otpStrategyType))
            throw new FundException(AuthenticationExceptionType.OTP_STRATEGY_IS_DISABLED, new Object[]{otpStrategyType.getTitle()});
    }

}
