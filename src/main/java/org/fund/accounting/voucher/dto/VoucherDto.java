package org.fund.accounting.voucher.dto;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.fund.common.FundUtils;
import org.fund.model.*;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidPersianDate;
import org.fund.validator.ValidateField;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
public class VoucherDto {
    private final JpaRepository repository;

    public VoucherDto(JpaRepository repository) {
        this.repository = repository;
    }

    private Long id;
    @NotEmpty(fieldName = "voucherTypeId")
    @ValidateField(fieldName = "voucherTypeId", entityClass = VoucherType.class)
    private Long voucherTypeId;
    @NotEmpty(fieldName = "fundBranchId")
    @ValidateField(fieldName = "fundBranchId", entityClass = FundBranch.class)
    private Long fundBranchId;
    @NotEmpty(fieldName = "voucherStatusId")
    @ValidateField(fieldName = "voucherStatusId", entityClass = VoucherStatus.class)
    private Long voucherStatusId;
    @NotEmpty(fieldName = "fundId")
    @ValidateField(fieldName = "fundId", entityClass = Fund.class)
    private Long fundId;
    private String code;
    @NotEmpty(fieldName = "voucherDate")
    @ValidPersianDate(fieldName = "voucherDate")
    private String voucherDate;
    private String comments;
    private Boolean isManual;
    private List<VoucherDetailDto> voucherDetails;

    public Voucher toVoucher() {
        ObjectMapper objectMapper = new ObjectMapper();
        Voucher voucher = objectMapper.convertValue(this, Voucher.class);
        voucher.setVoucherType(getVoucherType(voucherTypeId));
        voucher.setFundBranch(getFundBranch(fundBranchId));
        voucher.setVoucherStatus(getVoucherStatus(voucherStatusId));
        voucher.setFund(getFund(fundId));
        return voucher;
    }

    public List<VoucherDetail> toVoucherDetails() {
        if (FundUtils.isNull(voucherDetails) || voucherDetails.size() == 0)
            return null;
        List<VoucherDetail> voucherDetailList = new ArrayList<>();
        for (VoucherDetailDto voucherDetail : voucherDetails) {
            voucherDetailList.add(voucherDetail.toVoucherDetail());
        }
        return voucherDetailList;
    }

    private VoucherType getVoucherType(Long id) {
        return repository.findOne(VoucherType.class, id);
    }

    private FundBranch getFundBranch(Long id) {
        return repository.findOne(FundBranch.class, id);
    }

    private VoucherStatus getVoucherStatus(Long id) {
        return repository.findOne(VoucherStatus.class, id);
    }

    private Fund getFund(Long id) {
        return repository.findOne(Fund.class, id);
    }
}
