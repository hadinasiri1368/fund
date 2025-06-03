package org.fund.model;

import jakarta.persistence.*;
import lombok.*;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "AHA_VOUCHER_DETAIL")
@Entity(name = "voucherDetail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherDetail extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_VOUCHER_ID")
    private Voucher voucher;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_SUBSIDIARY_LEDGER_ID")
    private SubsidiaryLedger subsidiaryLedger;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_DETAIL_LEDGER_ID")
    private DetailLedger detailLedger;
    @Column(columnDefinition = "NUMBER(9)", name = "LINE_NUMBER", nullable = false)
    private Long lineNumber;
    @Column(columnDefinition = "NVARCHAR2(1000)", name = "COMMENTS")
    private String comments;
    @Column(columnDefinition = "NUMBER(18)", name = "DEBIT_AMOUNT", nullable = false)
    private Long debitAmount;
    @Column(columnDefinition = "NUMBER(18)", name = "CREDIT_AMOUNT", nullable = false)
    private Long creditAmount;
    @Column(columnDefinition = "NUMBER(18)", name = "REFERENCE_ID")
    private Long referenceId;
}
