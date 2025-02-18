package org.fund.authentication.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.fund.model.Permission;
import org.fund.model.Users;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserPermissionDto {
    private final JpaRepository repository;
    public UserPermissionDto(JpaRepository repository) {
        this.repository = repository;
    }
    @Setter
    @Getter
    private Long userId;
    @Setter
    @Getter
    private List<Long> permissionIds;

    public Users toUser() {
        return repository.findOne(Users.class, userId);
    }

    public List<Permission> toPermissions() {
        List<Permission> list=new ArrayList<>();
        for (Long permissionId : permissionIds) {
            list.add(repository.findOne(Permission.class, permissionId));
        }
        return list;
    }
}
