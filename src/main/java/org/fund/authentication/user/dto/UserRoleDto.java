package org.fund.authentication.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.fund.dto.DtoConvertible;
import org.fund.model.Role;
import org.fund.model.Users;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class UserRoleDto implements DtoConvertible {
    @NotEmpty(fieldName = "userId")
    private Long userId;
    @NotEmpty(fieldName = "roleIds")
    private List<Long> roleIds;

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        return repository.findOne(targetType, userId);
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        List<T> list = new ArrayList<>();
        for (Long roleId : roleIds) {
            list.add(repository.findOne(entityClass, roleId));
        }
        return list;
    }
}
