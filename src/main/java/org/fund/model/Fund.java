package org.fund.model;

import jakarta.persistence.*;
import lombok.*;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "AHA_FUND")
@Entity(name = "fund")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
public class Fund extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(columnDefinition = "NVARCHAR2(12)", name = "NAME", nullable = false)
    private String name;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_BASE_FUND", nullable = false)
    private Boolean isBaseFund;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_ETF", nullable = false)
    private Boolean isETF;

    @Override
    public boolean equals(Object obj) {
        return this.id.equals(((Fund) obj).id);
    }
}
