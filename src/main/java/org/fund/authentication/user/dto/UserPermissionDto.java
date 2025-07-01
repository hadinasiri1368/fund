package org.fund.authentication.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.fund.dto.DtoConvertible;
import org.fund.model.Permission;
import org.fund.model.Users;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class UserPermissionDto implements DtoConvertible {

    private Long userId;
    private List<Long> permissionIds;

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        return repository.findOne(targetType, userId);
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        List<T> list = new ArrayList<>();
        for (Long userId : permissionIds) {
            list.add(repository.findOne(entityClass, userId));
        }
        return list;
    }
}
