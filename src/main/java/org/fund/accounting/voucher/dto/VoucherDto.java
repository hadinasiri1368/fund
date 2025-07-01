package org.fund.accounting.voucher.dto;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.mapper.Mapper;
import org.fund.common.FundUtils;
import org.fund.dto.DtoConvertible;
import org.fund.dto.GenericDtoMapper;
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
public class VoucherDto implements DtoConvertible {

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

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        ObjectMapper objectMapper = new ObjectMapper();
        T entity = objectMapper.convertValue(this, targetType);

        if (entity instanceof Voucher voucher) {
            voucher.setVoucherType(getVoucherType(repository, voucherTypeId));
            voucher.setFundBranch(getFundBranch(repository, fundBranchId));
            voucher.setVoucherStatus(getVoucherStatus(repository, voucherStatusId));
            voucher.setFund(getFund(repository, fundId));
        }

        return entity;
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        List<T> entities = new ArrayList<>();
        for (VoucherDetailDto voucherDetail : voucherDetails) {
            entities.add(voucherDetail.toEntity(entityClass, repository));
        }
        return entities;
    }

    private VoucherType getVoucherType(JpaRepository repository, Long id) {
        return repository.findOne(VoucherType.class, id);
    }

    private FundBranch getFundBranch(JpaRepository repository, Long id) {
        return repository.findOne(FundBranch.class, id);
    }

    private VoucherStatus getVoucherStatus(JpaRepository repository, Long id) {
        return repository.findOne(VoucherStatus.class, id);
    }

    private Fund getFund(JpaRepository repository, Long id) {
        return repository.findOne(Fund.class, id);
    }
}
