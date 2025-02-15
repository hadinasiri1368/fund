package org.fund.params;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.fund.common.FundUtils;
import org.fund.model.*;
import org.fund.model.ParamsValueType;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class ParamDto {
    private final JpaRepository repository;
    public ParamDto(JpaRepository repository) {
        this.repository = repository;
    }

    private Long id;
    @NotEmpty(fieldName = "name")
    private String name;
    @NotEmpty(fieldName = "code")
    private String code;
    private String value;
    private String dataQuery;
    @NotEmpty(fieldName = "isActive")
    private Boolean isActive;
    @NotEmpty(fieldName = "isEditable")
    private Boolean isEditable;
    @NotEmpty(fieldName = "paramTypeId")
    @ValidateField(fieldName = "paramTypeId", entityClass = ParamsType.class)
    private Long paramsTypeId;
    @NotEmpty(fieldName = "fundId")
    @ValidateField(fieldName = "fundId", entityClass = Fund.class)
    private Long fundId;
    private Long detailLedgerId;
    private Long subsidiaryLedgerId;
    @NotEmpty(fieldName = "paramsValueTypeId")
    @ValidateField(fieldName = "paramsValueTypeId", entityClass = ParamsValueType.class)
    private Long paramsValueTypeId;
    private Boolean isGlobal;

    public Params toParams() {
        ObjectMapper objectMapper = new ObjectMapper();
        Params param = objectMapper.convertValue(this, Params.class);
        param.setParamsType(getParamsType(paramsTypeId));
        param.setFund(getFund(fundId));
        param.setDetailLedger(getDetailLedger(detailLedgerId));
        param.setSubsidiaryLedger(getSubsidiaryLedger(subsidiaryLedgerId));
        param.setParamsValueType(getParamsValueType(paramsValueTypeId));
        return param;
    }

    private ParamsType getParamsType(Long paramsTypeId) {
        return repository.findAll(ParamsType.class).stream()
                .filter(a -> a.getId().equals(paramsTypeId))
                .findFirst()
                .orElse(null);
    }

    private Fund getFund(Long fundId) {
        return repository.findAll(Fund.class).stream()
                .filter(a -> a.getId().equals(fundId))
                .findFirst()
                .orElse(null);
    }

    private DetailLedger getDetailLedger(Long detailLedgerId) {
        return repository.findAll(DetailLedger.class).stream()
                .filter(a -> a.getId().equals(detailLedgerId))
                .findFirst()
                .orElse(null);
    }

    private SubsidiaryLedger getSubsidiaryLedger(Long subsidiaryLedgerId) {
        return repository.findAll(SubsidiaryLedger.class).stream()
                .filter(a -> a.getId().equals(subsidiaryLedgerId))
                .findFirst()
                .orElse(null);
    }

    private ParamsValueType getParamsValueType(Long paramsValueType) {
        return repository.findAll(ParamsValueType.class).stream()
                .filter(a -> a.getId().equals(paramsValueType))
                .findFirst()
                .orElse(null);
    }
}
