package org.fund.authentication.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.fund.dto.BaseDto;
import org.fund.model.Role;
import org.fund.model.Users;
import org.fund.validator.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class UserRoleDto extends BaseDto {
    @NotEmpty(fieldName = "userId")
    private Long userId;
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
