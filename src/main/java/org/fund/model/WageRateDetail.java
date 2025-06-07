package org.fund.model;

import jakarta.persistence.*;
import lombok.*;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "AHA_WAGE_RATE_DETAIL")
@Entity(name = "wageRateDetail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
public class WageRateDetail extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_WAGE_RATE_ID")
    private WageRate wageRate;
    @Column(columnDefinition = "NUMBER(18)", name = "F_INDUSTRY_ID", nullable = false)
    private Long industryId;
    @Column(columnDefinition = "NUMBER(18)", name = "F_INSTRUMENT_ID", nullable = false)
    private Long instrumentId;
    @Column(columnDefinition = "NUMBER(18)", name = "F_BROKERAGE_ID", nullable = false)
    private Long brokerageid;
    @Column(columnDefinition = "VARCHAR2(10)", name = "ISSUE_DATE", nullable = false)
    private String issueDate;
    @Column(columnDefinition = "NUMBER", name = "BOURSE_CO", nullable = false)
    private Double bourseCo;
    @Column(columnDefinition = "NUMBER", name = "DEPOSIT_CO", nullable = false)
    private Double depositCo;
    @Column(columnDefinition = "NUMBER", name = "BOURSE_ORG", nullable = false)
    private Double bourseOrg;
    @Column(columnDefinition = "NUMBER", name = "IT_MANAGEMENT", nullable = false)
    private Double itManagement;
    @Column(columnDefinition = "NUMBER", name = "INTEREST", nullable = false)
    private Double interest;
    @Column(columnDefinition = "NUMBER", name = "TAX", nullable = false)
    private Double tax;
    @Column(columnDefinition = "NUMBER", name = "MAX_BOURSE_CO", nullable = false)
    private Double maxBourseCo;
    @Column(columnDefinition = "NUMBER", name = "MAX_DEPOSIT_CO", nullable = false)
    private Double maxDepositCo;
    @Column(columnDefinition = "NUMBER", name = "MAX_BOURSE_ORG", nullable = false)
    private Double maxBourseOrg;
    @Column(columnDefinition = "NUMBER", name = "MAX_IT_MANAGEMENT", nullable = false)
    private Double maxItManagement;
    @Column(columnDefinition = "NUMBER", name = "MAX_INTEREST", nullable = false)
    private Double maxInterest;
    @Column(columnDefinition = "NUMBER", name = "RAYAN_BOURSE", nullable = false)
    private Double rayanBourse;
    @Column(columnDefinition = "NUMBER", name = "MAX_RAYAN_BOURSE", nullable = false)
    private Double maxRayanBourse;
}
