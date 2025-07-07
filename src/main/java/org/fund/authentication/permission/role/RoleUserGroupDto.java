package org.fund.authentication.permission.role;

import lombok.Getter;
import lombok.Setter;
import org.fund.dto.DtoConvertible;
import org.fund.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class RoleUserGroupDto implements DtoConvertible {

    private Long userGroupId;
    private List<Long> roleIds;

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        return repository.findOne(targetType, userGroupId);
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        List<T> roles = new ArrayList<>();
        for (Long roleId : roleIds) {
            roles.add(repository.findOne(entityClass, roleId));
        }
        return roles;
    }
}
