package org.fund.model;

import jakarta.persistence.*;
import lombok.*;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "AHA_FUND_BANK_ACCOUNT")
@Entity(name = "fundBankAccount")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
public class FundBankAccount extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_FUND_ID")
    private Fund fund;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_BANK_ACCOUNT_ID")
    private BankAccount bankAccount;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_DETAIL_LEDGER_ID")
    private DetailLedger detailLedger;
}
