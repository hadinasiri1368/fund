package org.fund.authentication.user.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fund.model.Person;
import org.fund.model.Role;
import org.fund.model.Users;
import org.fund.model.VerificationCode;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotEmpty(fieldName = "isActive")
    private Boolean isActive;
    private Person person;
    @ValidateField(fieldName = "verificationCodeId", entityClass = VerificationCode.class)
    private Long verificationCodeId;
    @NotEmpty(fieldName = "username")
    private String username;
    @NotEmpty(fieldName = "password")
    private String password;

    public Users toUser() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, Users.class);
    }
}
