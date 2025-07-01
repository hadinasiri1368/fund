package org.fund.dto;

import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GenericDtoMapper {

    private final JpaRepository repository;

    public GenericDtoMapper(JpaRepository repository) {
        this.repository = repository;
    }

    public <T> T toEntity(Class<T> targetType, DtoConvertible dto) {
        return dto.toEntity(targetType, repository);
    }

    public <T> List<T> toEntityList(Class<T> entityClass, DtoConvertible dto) {
        return dto.toEntityList(entityClass, repository);
    }

    public <T> T toEntity(Class<T> targetType, DtoConvertible dto, Long id) {
        return dto.toEntity(targetType, repository, id);
    }

}

