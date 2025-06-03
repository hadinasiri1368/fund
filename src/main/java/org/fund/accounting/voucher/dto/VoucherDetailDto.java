package org.fund.accounting.voucher.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
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
@Component
public class VoucherDetailDto {
    private final JpaRepository repository;

    public VoucherDetailDto(JpaRepository repository) {
        this.repository = repository;
    }

    private Long id;
    @NotEmpty(fieldName = "voucherId")
    @ValidateField(fieldName = "voucherId", entityClass = Voucher.class)
    private Long voucherId;
    @NotEmpty(fieldName = "subsidiaryLedgerId")
    @ValidateField(fieldName = "subsidiaryLedgerId", entityClass = SubsidiaryLedger.class)
    private Long subsidiaryLedgerId;
    @ValidateField(fieldName = "subsidiaryLedgerId", entityClass = DetailLedger.class)
    private Long detailLedgerId;
    private String comments;
    @NotEmpty(fieldName = "debitAmoun")
    private Long debitAmount;
    @NotEmpty(fieldName = "creditAmount")
    private Long creditAmount;

    public VoucherDetail toVoucherDetail() {
        ObjectMapper objectMapper = new ObjectMapper();
        VoucherDetail voucherDetail = objectMapper.convertValue(this, VoucherDetail.class);
        voucherDetail.setVoucher(getVoucher(voucherId));
        voucherDetail.setSubsidiaryLedger(getSubsidiaryLedger(subsidiaryLedgerId));
        voucherDetail.setDetailLedger(getDetailLedger(detailLedgerId));
        return voucherDetail;
    }

    private Voucher getVoucher(Long id) {
        return repository.findOne(Voucher.class, id);
    }

    private SubsidiaryLedger getSubsidiaryLedger(Long id) {
        return repository.findOne(SubsidiaryLedger.class, id);
    }

    private DetailLedger getDetailLedger(Long id) {
        return repository.findOne(DetailLedger.class, id);
    }
}
