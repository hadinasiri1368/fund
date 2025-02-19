package org.fund.model;

import jakarta.persistence.*;
import lombok.*;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "AHA_MMTP_CONFIG")
@Entity(name = "mmtpConfig")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
public class MmtpConfig extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(columnDefinition = "CHAR(3)", name = "BROKERAGE_CODE", nullable = false)
    private String brokerageCode;
    @Column(columnDefinition = "CHAR(1)", name = "APP_ID", nullable = false)
    private String appId;
    @Column(columnDefinition = "CHAR(5)", name = "TRADER_ID", nullable = false)
    private String traderId;
    @Column(columnDefinition = "CHAR(12)", name = "INS_MAX_LCODE_FUND2")
    private String insMaxLcodeFund2;
    @Column(columnDefinition = "CHAR(16)", name = "FUND_ACCOUNT_NUMBER")
    private String fundAccountNumber;
    @Column(columnDefinition = "CHAR(5)", name = "INS_MNEMONIC_CODE_FUND")
    private String insMnemonicCodeFund;
    @Column(columnDefinition = "NUMBER(1)", name = "RESERVE_ORDER_ORIGIN")
    private Integer reserveOrderOrigin;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_FUND_ID")
    private Fund fund;
}
