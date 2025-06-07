package org.fund.model;

import jakarta.persistence.*;
import lombok.*;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "AHA_WAGE_RATE")
@Entity(name = "wageRate")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
public class WageRate extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_FUND_ID")
    private Fund fund;
    @Column(columnDefinition = "NUMBER(18)", name = "F_INSTRUMENT_TYPE_ID", nullable = false)
    private Long instrumentTypeId;
    @Column(columnDefinition = "NUMBER(18)", name = "F_INST_TYPE_DERIVATIVES_ID", nullable = false)
    private Long instTypeDerivativesId;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_OTC", nullable = false)
    private Boolean isOtc;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_PURCHASE", nullable = false)
    private Boolean isPurchase;
}
