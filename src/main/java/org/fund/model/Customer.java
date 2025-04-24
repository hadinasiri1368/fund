package org.fund.model;

import jakarta.persistence.*;
import lombok.*;
import org.fund.config.cache.CacheableEntity;

@Table(name = "AHA_CUSTOMER")
@Entity(name = "customer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@CacheableEntity
public class Customer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_DETAIL_LEDGER_ID")
    private DetailLedger detailLedger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_CUSTOMER_STATUS_ID")
    private CustomerStatus customerStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_PERSON_ID")
    private Person person;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_CUSTOMER_BANK_ACCOUNT_ID")
    private CustomerBankAccount customerBankAccount;
    @Column(columnDefinition = "NVARCHAR2(1000)", name = "COMMENTS")
    private String comments;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_SMS_SEND")
    private boolean isSmsSend;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_SEJAM")
    private boolean isSejam;
    @Column(columnDefinition = "NUMBER", name = "PROFIT_RATE")
    private float profitRate;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_PROFIT_ISSUE")
    private boolean isProfitIssue;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_VAT")
    private boolean isVat;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_EPAYMENT_CUSTOMER")
    private boolean isEpaymentCustomer;
}
