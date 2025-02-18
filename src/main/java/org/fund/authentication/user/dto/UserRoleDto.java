package org.fund.authentication.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.fund.model.Role;
import org.fund.model.Users;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRoleDto {
    private final JpaRepository repository;
    public UserRoleDto(final JpaRepository repository) {
        this.repository = repository;
    }
    @Setter
    @Getter
    @NotEmpty(fieldName = "userId")
    private Long userId;
    @Setter
    @Getter
    @NotEmpty(fieldName = "roleIds")
    private List<Long> roleIds;

    public Users toUser() {
        return repository.findOne(Users.class, userId);
    }

    public List<Role> toRoles() {
        List<Role> list = new ArrayList<>();
        for (Long roleId : roleIds) {
            list.add(repository.findOne(Role.class, roleId));
        }
        return list;
    }
}
