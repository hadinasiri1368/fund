package org.fund.administration.wageRate.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.fund.dto.DtoConvertible;
import org.fund.model.DetailLedger;
import org.fund.model.Fund;
import org.fund.model.WageRate;
import org.fund.model.view.external.InstrumentType;
import org.fund.model.view.external.InstrumentTypeDerivatives;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
public class WageRateDto implements DtoConvertible {
    private Long id;
    @NotEmpty(fieldName = "fundId")
    @ValidateField(fieldName = "id", entityClass = Fund.class)
    private Long fundId;
    @NotEmpty(fieldName = "instrumentTypeId")
    @ValidateField(fieldName = "id", entityClass = InstrumentType.class)
    private Long instrumentTypeId;
    @NotEmpty(fieldName = "instTypeDerivativesId")
    @ValidateField(fieldName = "id", entityClass = InstrumentTypeDerivatives.class)
    private Long instTypeDerivativesId;
    private Boolean isOtc;
    private Boolean isPurchase;
    private WageRateDetailDto wageRateDetail;

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        ObjectMapper objectMapper = new ObjectMapper();
        T entity = objectMapper.convertValue(this, targetType);

        if (entity instanceof WageRate wageRate) {
            wageRate.setFund(getFund(repository));
        }

        return entity;
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        return List.of();
    }

    private Fund getFund(JpaRepository repository) {
        return repository.findOne(Fund.class, fundId);
    }
}
