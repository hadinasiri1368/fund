package org.fund.authentication.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.fund.model.UserGroup;
import org.fund.model.Users;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserGroupDetailDto {
    private final JpaRepository repository;

    public UserGroupDetailDto(JpaRepository repository) {
        this.repository = repository;
    }
    @Setter
    @Getter
    @NotEmpty(fieldName = "userGroupId")
    private Long userGroupId;
    @Setter
    @Getter
    @NotEmpty(fieldName = "userIds")
    private List<Long> userIds;

    public UserGroup toUserGroup() {
        return repository.findOne(UserGroup.class, userGroupId);
    }

    public List<Users> toUsers() {
        List<Users> list = new ArrayList<>();
        for (Long userId : userIds) {
            list.add(repository.findOne(Users.class, userId));
        }
        return list;
    }
}
