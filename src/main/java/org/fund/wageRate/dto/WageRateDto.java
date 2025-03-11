package org.fund.wageRate.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.fund.model.Fund;
import org.fund.model.Params;
import org.fund.model.WageRate;
import org.fund.model.view.external.Industry;
import org.fund.model.view.external.Instrument;
import org.fund.model.view.external.InstrumentType;
import org.fund.model.view.external.InstrumentTypeDerivatives;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class WageRateDto {
    private final JpaRepository repository;

    public WageRateDto(JpaRepository repository) {
        this.repository = repository;
    }

    private Long id;
    @NotEmpty(fieldName = "fundId")
    @ValidateField(fieldName = "fundId", entityClass = Fund.class)
    private Long fundId;
    @NotEmpty(fieldName = "instrumentTypeId")
    @ValidateField(fieldName = "instrumentTypeId", entityClass = InstrumentType.class)
    private Long instrumentTypeId;
    @NotEmpty(fieldName = "instTypeDerivativesId")
    @ValidateField(fieldName = "instTypeDerivativesId", entityClass = InstrumentTypeDerivatives.class)
    private Long instTypeDerivativesId;
    private Boolean isOtc;
    private Boolean isPurchase;
    private WageRateDetailDto wageRateDetail;

    public WageRate toWageRate() {
        ObjectMapper objectMapper = new ObjectMapper();
        WageRate wageRate = objectMapper.convertValue(this, WageRate.class);
        wageRate.setFund(getFund());
        return wageRate;
    }

    private Fund getFund() {
        return repository.findOne(Fund.class, fundId);
    }
}
