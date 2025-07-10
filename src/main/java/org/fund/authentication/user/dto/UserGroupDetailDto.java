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

    @NotEmpty(fieldName = "userId")
    private Long userId;
    @NotEmpty(fieldName = "userGroupIds")
    private List<Long> userGroupIds;

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        return repository.findOne(targetType, userId);
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        List<T> list = new ArrayList<>();
        for (Long userId : userGroupIds) {
            list.add(repository.findOne(entityClass, userId));
        }
        return list;
    }
}
