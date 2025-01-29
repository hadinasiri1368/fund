package org.fund.authentication.otp.dto;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpResponseDto implements Serializable {
    private String verificationCode;

    private Long expirationTime;

    private Integer numberOfAttempts;

    private Boolean messageSend;
}
