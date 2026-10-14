package org.fund.administration.params;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.fund.dto.DtoConvertible;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParamTypeDto implements DtoConvertible {

    private Long id;
    @NotEmpty(fieldName = "name")
    private String name;

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        return null;
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        return List.of();
    }
}
