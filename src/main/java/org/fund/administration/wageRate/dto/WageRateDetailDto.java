package org.fund.administration.wageRate.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.fund.model.WageRate;
import org.fund.model.WageRateDetail;
import org.fund.model.view.external.Industry;
import org.fund.model.view.external.Instrument;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class WageRateDetailDto {
    private final JpaRepository repository;

    public WageRateDetailDto(JpaRepository repository) {
        this.repository = repository;
    }

    private Long id;
    @ValidateField(fieldName = "wageRateId", entityClass = WageRate.class)
    private Long wageRateId;
    @ValidateField(fieldName = "industryId", entityClass = Industry.class)
    private Long industryId;
    @ValidateField(fieldName = "instrumentId", entityClass = Instrument.class)
    private Long instrumentId;
    @NotEmpty(fieldName = "issueDate")
    private String issueDate;
    @NotEmpty(fieldName = "bourseCo")
    private Double bourseCo;
    @NotEmpty(fieldName = "depositCo")
    private Double depositCo;
    @NotEmpty(fieldName = "bourseOrg")
    private Double bourseOrg;
    @NotEmpty(fieldName = "itManagement")
    private Double itManagement;
    @NotEmpty(fieldName = "interest")
    private Double interest;
    @NotEmpty(fieldName = "tax")
    private Double tax;
    @NotEmpty(fieldName = "maxBourseCo")
    private Double maxBourseCo;
    @NotEmpty(fieldName = "maxDepositCo")
    private Double maxDepositCo;
    @NotEmpty(fieldName = "maxBourseOrg")
    private Double maxBourseOrg;
    @NotEmpty(fieldName = "maxItManagement")
    private Double maxItManagement;
    @NotEmpty(fieldName = "maxInterest")
    private Double maxInterest;
    @NotEmpty(fieldName = "rayanBourse")
    private Double rayanBourse;
    @NotEmpty(fieldName = "maxRayanBourse")
    private Double maxRayanBourse;

    public WageRateDetail toWageRateDetail() {
        ObjectMapper objectMapper = new ObjectMapper();
        WageRateDetail wageRateDetail = objectMapper.convertValue(this, WageRateDetail.class);
        wageRateDetail.setWageRate(getWageRate());
        return wageRateDetail;
    }

    private WageRate getWageRate() {
        return repository.findOne(WageRate.class, wageRateId);
    }
}
