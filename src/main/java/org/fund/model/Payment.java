package org.fund.model;

import jakarta.persistence.*;
import lombok.*;
import org.fund.config.cache.CacheableEntity;
import org.fund.model.view.external.Bank;


import java.io.Serializable;

@Table(name = "AHA_PAYMENT")
@Entity(name = "payment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
public class Payment extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(columnDefinition = "NVARCHAR2(300)", name = "CODE", nullable = false)
    private String code;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_PAYMENT_REASON_ID")
    private PaymentReason paymentReason;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_FROM_SUBSIDIARY_LEDGER_ID")
    private SubsidiaryLedger fromSubsidiaryLedger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_TO_SUBSIDIARY_LEDGER_ID")
    private SubsidiaryLedger toSubsidiaryLedger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_FROM_DETAIL_LEDGER_ID")
    private DetailLedger fromDetailLedger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_FUND_ID")
    private Fund fund;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_BANK_ID")
    private Bank bank;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_PAYMENT_TYPE_ID")
    private PaymentType paymentType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_PAYMENT_STATUS_ID")
    private PaymentStatus paymentStatus ;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_PAYMENT_ORIGIN_ID")
    private PaymentOrigin paymentOrigin ;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_BANK_ACCOUNT_ID")
    private BankAccount bankAccount ;
    @Column(columnDefinition = "NVARCHAR2(10)", name = "PAYMENT_DATE", nullable = false)
    private String paymentDate;
    @Column(columnDefinition = "NVARCHAR2(300)", name = "COMMENTS")
    private String comments;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_MANUAL", nullable = false)
    private Boolean isManual;
    @Column(columnDefinition = "NUMBER(1)", name = "SENT_TO_BANK", nullable = false)
    private Boolean sentToBank;
    @Column(columnDefinition = "NVARCHAR2(50)", name = "UUID")
    private String uuid;
}
