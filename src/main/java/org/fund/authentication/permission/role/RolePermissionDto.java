package org.fund.authentication.permission.role;

import lombok.*;
import org.fund.model.Permission;
import org.fund.model.Role;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class RolePermissionDto {
    private final JpaRepository repository;

    public RolePermissionDto(JpaRepository repository) {
        this.repository = repository;
    }

    @Setter
    @Getter
    @NotEmpty(fieldName = "roleId")
    @ValidateField(fieldName = "roleId", entityClass = Role.class)
    private Long roleId;
    @Setter
    @Getter
    @NotEmpty(fieldName = "permissionIds")
    private List<Long> permissionIds;

    public Role toRole() {
        return repository.findOne(Role.class, roleId);
    }

    public List<Permission> toPermissions() {
        List<Permission> permissions = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            permissions.add(repository.findOne(Permission.class, permissionId));
        }
        return permissions;
    }
}
