package org.fund.authentication.user.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fund.dto.DtoConvertible;
import org.fund.model.Params;
import org.fund.model.UserGroup;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupDto implements DtoConvertible {
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
