package org.fund.authentication.permission;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.fund.dto.DtoConvertible;
import org.fund.model.Permission;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Service
@Getter
@Builder
public class PermissionDto implements DtoConvertible {
    private Long id;
    @NotEmpty(fieldName = "name")
    private String name;
    @NotEmpty(fieldName = "url")
    private String url;
    @NotEmpty(fieldName = "isSensitive")
    private Boolean isSensitive;

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
