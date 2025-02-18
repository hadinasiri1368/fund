package org.fund.authentication.user.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fund.model.Params;
import org.fund.model.UserGroup;
import org.fund.validator.NotEmpty;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupDto {
    private Long id;
    @NotEmpty(fieldName = "name")
    private String name;

    public UserGroup toUserGroup() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, UserGroup.class);
    }
}
