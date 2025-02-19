package org.fund.basicData.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.fund.model.Fund;
import org.fund.model.Params;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundDto {
    private Long id;
    private String name;
    private Boolean isBaseFund;
    private Boolean isActive;
    private Boolean isETF;

    public Fund toFund() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, Fund.class);
    }
}
