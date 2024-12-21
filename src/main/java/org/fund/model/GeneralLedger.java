package org.fund.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Table(name = "AHA_GENERAL_LEDGER")
@Entity(name = "generalLedger")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneralLedger extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(columnDefinition = "NVARCHAR2(50)", name = "NAME", nullable = false)
    private String name;
    @Column(columnDefinition = "NVARCHAR2(12)", name = "CODE", nullable = false)
    private String code;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_AHA_GENERAL_LEDGER_TYPE_ID")
    private GeneralLedgerType generalLedgerType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_AHA_ACCOUNT_NATURE_ID")
    private AccountNature accountNature;
}
