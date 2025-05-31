package org.fund.model;

import jakarta.persistence.*;
import lombok.*;
import org.fund.config.cache.CacheableEntity;
import org.fund.model.view.external.Bank;

import java.io.Serializable;

@Table(name = "AHA_PAYMENT_DETAIL")
@Entity(name = "paymentDetail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDetail extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_PAYMENT_ID")
    private Payment payment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_DETAIL_LEDGER_ID")
    private DetailLedger detailLedger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_PAYMENT_STATUS_ID")
    private PaymentStatus paymentStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_BANK_ACCOUNT_ID")
    private BankAccount bankAccount;
    @Column(columnDefinition = "NVARCHAR2(50)", name = "UUID")
    private String uuid;
    @Column(columnDefinition = "NUMBER", name = "F_ORDER_ID")
    private Long orderId;
    @Column(columnDefinition = "NUMBER", name = "AMOUNT", nullable = false)
    private Long amount;
    @Column(columnDefinition = "NVARCHAR2(300)", name = "COMMENTS")
    private String comments;
}
