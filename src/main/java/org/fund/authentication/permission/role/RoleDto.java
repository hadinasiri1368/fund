package org.fund.authentication.permission.role;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.fund.model.Params;
import org.fund.model.Role;
import org.fund.validator.NotEmpty;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDto {
    private Long id;
    @NotEmpty(fieldName = "name")
    private String name;

    public Role toRole() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, Role.class);
    }
}
