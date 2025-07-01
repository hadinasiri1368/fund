package org.fund.dto;

import org.fund.repository.JpaRepository;

import java.util.List;

public interface DtoConvertible {
    <T> T toEntity(Class<T> targetType, JpaRepository repository);

    <T> T toEntity(Class<T> targetType, JpaRepository repository, Long id);

    <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository);
}
