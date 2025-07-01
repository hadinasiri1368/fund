package org.fund.administration.params;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.fund.common.FundUtils;
import org.fund.dto.DtoConvertible;
import org.fund.model.*;
import org.fund.model.ParamsValueType;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
public class ParamDto implements DtoConvertible {
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

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        ObjectMapper objectMapper = new ObjectMapper();
        T entity = objectMapper.convertValue(this, targetType);

        if (entity instanceof Params param) {
            param.setParamsType(getParamsType(repository, paramsTypeId));
            param.setFund(getFund(repository, fundId));
            param.setDetailLedger(getDetailLedger(repository, detailLedgerId));
            param.setSubsidiaryLedger(getSubsidiaryLedger(repository, subsidiaryLedgerId));
            param.setParamsValueType(getParamsValueType(repository, paramsValueTypeId));
        }

        return entity;
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        return List.of();
    }

    private ParamsType getParamsType(JpaRepository repository, Long paramsTypeId) {
        return repository.findAll(ParamsType.class).stream()
                .filter(a -> a.getId().equals(paramsTypeId))
                .findFirst()
                .orElse(null);
    }

    private Fund getFund(JpaRepository repository, Long fundId) {
        return repository.findAll(Fund.class).stream()
                .filter(a -> a.getId().equals(fundId))
                .findFirst()
                .orElse(null);
    }

    private DetailLedger getDetailLedger(JpaRepository repository, Long detailLedgerId) {
        return repository.findAll(DetailLedger.class).stream()
                .filter(a -> a.getId().equals(detailLedgerId))
                .findFirst()
                .orElse(null);
    }

    private SubsidiaryLedger getSubsidiaryLedger(JpaRepository repository, Long subsidiaryLedgerId) {
        return repository.findAll(SubsidiaryLedger.class).stream()
                .filter(a -> a.getId().equals(subsidiaryLedgerId))
                .findFirst()
                .orElse(null);
    }

    private ParamsValueType getParamsValueType(JpaRepository repository, Long paramsValueType) {
        return repository.findAll(ParamsValueType.class).stream()
                .filter(a -> a.getId().equals(paramsValueType))
                .findFirst()
                .orElse(null);
    }
}
