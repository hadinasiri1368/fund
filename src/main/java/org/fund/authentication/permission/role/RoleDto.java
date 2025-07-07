package org.fund.authentication.permission.role;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.fund.dto.DtoConvertible;
import org.fund.model.Params;
import org.fund.model.Role;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDto implements DtoConvertible {
    private Long id;
    @NotEmpty(fieldName = "name")
    private String name;

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
