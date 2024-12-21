package org.fund.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Table(name = "AHA_DETAIL_LEDGER")
@Entity(name = "detailLedger")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailLedger extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(columnDefinition = "NVARCHAR2(12)", name = "NAME", nullable = false)
    private String name;
    @Column(columnDefinition = "NVARCHAR2(12)", name = "CODE", nullable = false)
    private String code;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_DETAIL_LEDGER_TYPE_ID")
    private DetailLedgerType detailLedgerType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_ACCOUNT_NATURE_ID")
    private AccountNature accountNature;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;
}