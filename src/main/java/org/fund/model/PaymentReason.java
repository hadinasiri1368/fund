package org.fund.model;

import jakarta.persistence.*;
import lombok.*;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;
@Table(name = "AHA_PAYMENT_REASON")
@Entity(name = "paymentReason")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
public class PaymentReason extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    @Column(columnDefinition = "NVARCHAR2(12)", name = "NAME", nullable = false)
    private String name;
    @Column(columnDefinition = "NUMBER(1)", name = "SYSTEM_DEFINED", nullable = false)
    private Boolean systemDefined;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FROM_SUBSIDIARY_LEDGER_ID")
    private SubsidiaryLedger fromSubsidiaryLedger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TO_SUBSIDIARY_LEDGER_ID")
    private SubsidiaryLedger toSubsidiaryLedger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FROM_DETAIL_LEDGER_ID")
    private DetailLedger fromDetailLedger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TO_DETAIL_LEDGER_ID")
    private DetailLedger toDetailLedger;
}
