package org.fund.authentication.permission.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.fund.model.Permission;
import org.fund.model.Role;
import org.fund.model.Users;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
public class UserPermissionDto {
    private final JpaRepository repository;

    public UserPermissionDto(JpaRepository repository) {
        this.repository = repository;
    }

    @NotEmpty(fieldName = "userId")
    @ValidateField(fieldName = "userId", entityClass = Users.class)
    private Long userId;
    @NotEmpty(fieldName = "permissionIds")
    private List<Long> permissionIds;

    public Users toUser() {
        return repository.findOne(Users.class, userId);
    }

    public List<Permission> toPermissions() {
        List<Permission> permissions = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            permissions.add(repository.findOne(Permission.class, permissionId));
        }
        return permissions;
    }
}
