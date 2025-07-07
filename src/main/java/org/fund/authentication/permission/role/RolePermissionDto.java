package org.fund.authentication.permission.role;

import lombok.*;
import org.fund.accounting.voucher.dto.VoucherDetailDto;
import org.fund.dto.DtoConvertible;
import org.fund.model.Permission;
import org.fund.model.Role;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class RolePermissionDto implements DtoConvertible {
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

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        return repository.findOne(targetType, roleId);
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        List<T> entities = new ArrayList<>();
        for (Long id : permissionIds) {
            entities.add(repository.findOne(entityClass, id));
        }
        return entities;
    }
}
