package org.fund.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fund.config.cache.CacheableEntity;

@Table(name = "AHA_CUSTOMER_STATUS")
@Entity(name = "customerStatus")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@CacheableEntity
public class CustomerStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(columnDefinition = "NVARCHAR2(100)", name = "NAME")
    private String name;
}
