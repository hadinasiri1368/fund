package org.fund.authentication;

import org.fund.authentication.otp.constant.OtpStrategyType;
import org.fund.authentication.otp.dto.OtpRequestDto;
import org.fund.constant.Consts;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@Validated
public class AuthenticationController {
    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping(path = "/login")
    public String login(@RequestBody LoginDto loginDto) throws Exception {
        return service.login(loginDto);
    }

    @GetMapping(Consts.DEFAULT_PREFIX_API_URL + Consts.DEFAULT_VERSION_API_URL + "/basicData/getOtpStrategies")
    public Map<Integer, String> getOtpStrategies() {
        return service.getOtpStrategyTypeList().stream()
                .collect(Collectors.toMap(
                        item -> item.getId(),
                        OtpStrategyType::getTitle
                ));
    }

    @PostMapping(path = "/sendOtpForLogin")
    public void sendOtpForLogin(@RequestBody OtpRequestDto otpRequestDto) {
        service.sendOtpForLogin(otpRequestDto);
    }
}
