package org.fund.authentication.permission;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.fund.model.Permission;
import org.fund.validator.NotEmpty;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@Service
@Getter
@Builder
public class PermissionDto {
    private Long id;
    @NotEmpty(fieldName = "name")
    private String name;
    @NotEmpty(fieldName = "url")
    private String url;
    @NotEmpty(fieldName = "isSensitive")
    private Boolean isSensitive;

    public Permission toPermission() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, Permission.class);
    }
}
