package org.fund.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fund.baseInformation.customer.dto.CustomerStatusDto;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "AHA_CUSTOMER_STATUS")
@Entity(name = "customerStatus")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@CacheableEntity
public class CustomerStatus implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    @Column(columnDefinition = "NVARCHAR2(100)", name = "NAME")
    private String name;

    public CustomerStatusDto toDto() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, CustomerStatusDto.class);
    }
}
