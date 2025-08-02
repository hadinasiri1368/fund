package org.fund.authentication.user.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fund.dto.DtoConvertible;
import org.fund.model.Person;
import org.fund.model.Role;
import org.fund.model.Users;
import org.fund.model.VerificationCode;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements DtoConvertible {
    private Long id;
    @NotEmpty(fieldName = "isActive")
    private Boolean isActive;
    private Person person;
    @ValidateField(fieldName = "id", entityClass = VerificationCode.class)
    private Long verificationCodeId;
    @NotEmpty(fieldName = "username")
    private String username;
    @NotEmpty(fieldName = "password")
    private String password;

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, targetType);
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        return List.of();
    }
}
