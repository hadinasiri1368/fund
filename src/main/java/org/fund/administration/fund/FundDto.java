package org.fund.administration.fund;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.fund.model.Fund;
import org.fund.model.Params;
import org.fund.validator.NotEmpty;

@Getter
@Setter
@Builder
public class FundDto {
    private Long id;
    @NotEmpty(fieldName = "name")
    private String name;
    @NotEmpty(fieldName = "isETF")
    private Boolean isETF;

    public Fund toFund() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, Fund.class);
    }
}
