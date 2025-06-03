package org.fund.model;

import jakarta.persistence.*;
import lombok.*;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "AHA_VOUCHER")
@Entity(name = "voucher")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Voucher extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_VOUCHER_TYPE_ID")
    private VoucherType voucherType;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_FUND_BRANCH_ID")
    private FundBranch fundBranch;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_VOUCHER_STATUS_ID")
    private VoucherStatus voucherStatus;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_FUND_ID")
    private Fund fund;
    @Column(columnDefinition = "NVARCHAR2(50)", name = "CODE", nullable = false)
    private String code;
    @Column(columnDefinition = "NVARCHAR2(10)", name = "VOUCHER_DATE", nullable = false)
    private String voucherDate;
    @Column(columnDefinition = "NVARCHAR2(1000)", name = "COMMENTS")
    private String comments;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_MANUAL", nullable = false)
    private Boolean isManual;
}
