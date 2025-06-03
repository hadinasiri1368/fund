package org.fund.accounting.voucher.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fund.model.DetailLedger;
import org.fund.model.SubsidiaryLedger;
import org.fund.model.Voucher;
import org.fund.model.VoucherDetail;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VoucherDetailResponseDto {
    private Long detailId;
    private Long voucherId;
    private Long subsidiaryLedgerId;
    private String subsidiaryLedgerName;
    private Long detailLedgerId;
    private String detailLedgerName;
    private String detailComments;
    private Long debitAmount;
    private Long creditAmount;
}
