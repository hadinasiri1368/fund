package org.fund.authentication.permission.role;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.fund.administration.params.ParamTypeDto;
import org.fund.model.ParamsType;
import org.fund.dto.DtoConvertible;
import org.fund.model.Role;
import org.fund.model.RoleParamsType;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;

import java.util.List;

@Getter
@Setter
public class RoleParamsTypeDto implements DtoConvertible {
    private Long id;
    @NotEmpty(fieldName = "roleId")
    @ValidateField(fieldName = "id", entityClass = Role.class)
    private Long roleId;
    private ParamTypeDto paramTypeDto;

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        ObjectMapper objectMapper = new ObjectMapper();
        T entity = objectMapper.convertValue(this, targetType);
        if (entity instanceof RoleParamsType roleParamsType) {
            roleParamsType.setRole(getRole(repository, roleId));
            roleParamsType.setParamsType(getParamType(repository, paramTypeDto));
        }

        return entity;
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        return List.of();
    }

    private Role getRole(JpaRepository repository, Long roleId) {
        return repository.findAll(Role.class).stream()
                .filter(a -> a.getId().equals(roleId))
                .findFirst()
                .orElse(null);
    }

    private ParamsType getParamType(JpaRepository repository, ParamTypeDto paramTypeDto) {
        return repository.findOne(ParamsType.class, paramTypeDto.getId());
    }
}
