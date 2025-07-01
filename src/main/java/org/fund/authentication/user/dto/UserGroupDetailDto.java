package org.fund.authentication.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.fund.dto.DtoConvertible;
import org.fund.model.UserGroup;
import org.fund.model.Users;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class UserGroupDetailDto implements DtoConvertible {

    @NotEmpty(fieldName = "userGroupId")
    private Long userGroupId;
    @NotEmpty(fieldName = "userIds")
    private List<Long> userIds;

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        return repository.findOne(targetType, userGroupId);
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        List<T> list = new ArrayList<>();
        for (Long userId : userIds) {
            list.add(repository.findOne(entityClass, userId));
        }
        return list;
    }
}
