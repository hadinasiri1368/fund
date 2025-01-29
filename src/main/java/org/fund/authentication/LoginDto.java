package org.fund.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.fund.model.Users;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@Service
@Getter
@Builder
public class LoginDto {
    @ValidateField(fieldName = "username", entityClass = Users.class)
    private String username;
    @NotEmpty(fieldName = "password")
    private String password;
    private Integer otpStrategyTypeId;
    private String otpCode;
}
