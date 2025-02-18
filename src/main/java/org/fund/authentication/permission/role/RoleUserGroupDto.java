package org.fund.authentication.permission.role;

import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;
import org.fund.authentication.user.dto.UserGroupDto;
import org.fund.model.Role;
import org.fund.model.UserGroup;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleUserGroupDto {
    private final JpaRepository repository;

    public RoleUserGroupDto(JpaRepository repository) {
        this.repository = repository;
    }
    @Setter
    @Getter
    private Long userGroupId;
    @Setter
    @Getter
    private List<Long> roleIds;

    public UserGroup toUserGroup() {
        return repository.findOne(UserGroup.class, userGroupId);
    }

    public List<Role> toRoles() {
        List<Role> roles = new ArrayList<>();
        for (Long roleId : roleIds) {
            roles.add(repository.findOne(Role.class, roleId));
        }
        return roles;
    }
}
