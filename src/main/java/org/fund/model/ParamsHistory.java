package org.fund.model;

import jakarta.persistence.*;
import lombok.*;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "AHA_PARAMS_HISTORY")
@Entity(name = "paramsHistory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
public class ParamsHistory extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_PARAMS_ID")
    private Params params;
    @Column(columnDefinition = "VARCHAR2(400)", name = "VALUE", nullable = false)
    private String value;
    @Column(columnDefinition = "CHAR(10)", name = "EFFECTIVE_DATE", nullable = false)
    private String effectiveDate;
}
