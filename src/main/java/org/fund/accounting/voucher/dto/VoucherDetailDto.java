package org.fund.accounting.voucher.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.fund.dto.DtoConvertible;
import org.fund.model.DetailLedger;
import org.fund.model.SubsidiaryLedger;
import org.fund.model.Voucher;
import org.fund.model.VoucherDetail;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;

import java.util.List;

@Getter
@Setter
public class VoucherDetailDto implements DtoConvertible {
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

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        ObjectMapper objectMapper = new ObjectMapper();
        T entity = objectMapper.convertValue(this, targetType);

        if (entity instanceof VoucherDetail voucherDetail) {
            voucherDetail.setVoucher(getVoucher(repository, voucherId));
            voucherDetail.setSubsidiaryLedger(getSubsidiaryLedger(repository, subsidiaryLedgerId));
            voucherDetail.setDetailLedger(getDetailLedger(repository, detailLedgerId));
        }

        return entity;
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        return List.of();
    }

    private Voucher getVoucher(JpaRepository repository, Long id) {
        return repository.findOne(Voucher.class, id);
    }

    private SubsidiaryLedger getSubsidiaryLedger(JpaRepository repository, Long id) {
        return repository.findOne(SubsidiaryLedger.class, id);
    }

    private DetailLedger getDetailLedger(JpaRepository repository, Long id) {
        return repository.findOne(DetailLedger.class, id);
    }
}
