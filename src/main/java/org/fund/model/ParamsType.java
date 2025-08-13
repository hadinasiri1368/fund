package org.fund.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.fund.administration.params.ParamTypeDto;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "AHA_PARAMS_TYPE")
@Entity(name = "paramsType")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
public class ParamsType extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    @Column(columnDefinition = "NVARCHAR2(50)", name = "NAME", nullable = false)
    private String name;

    public ParamTypeDto toDto() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, ParamTypeDto.class);
    }
}
