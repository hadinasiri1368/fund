package org.fund.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.fund.authentication.permission.role.RoleParamsTypeDto;
import org.fund.baseInformation.customer.dto.CustomerBankAccountDto;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "AHA_ROLE_PARAMS_TYPE")
@Entity(name = "RoleParamsType")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
public class RoleParamsType extends  BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_ROLE_ID")
    private Role role;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_PARAMS_TYPE_ID")
    private ParamsType paramsType;

    public RoleParamsTypeDto toDto() {
        ObjectMapper objectMapper = new ObjectMapper();
        RoleParamsTypeDto roleParamsTypeDto = objectMapper.convertValue(this, RoleParamsTypeDto.class);
        roleParamsTypeDto.setRoleId(role.getId());
        roleParamsTypeDto.setParamTypeDto(paramsType.toDto());
        return roleParamsTypeDto;
    }

}
