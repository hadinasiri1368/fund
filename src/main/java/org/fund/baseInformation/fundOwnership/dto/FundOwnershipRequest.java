package org.fund.baseInformation.fundOwnership.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.fund.model.FundBranch;
import org.fund.model.FundOwnership;
import org.fund.model.view.external.BourseFund;
import org.fund.model.view.external.Instrument;
import org.fund.validator.NotEmpty;

@Getter
@Setter
@Builder
public class FundOwnershipRequest {
    private Long id;
    private Long bourseFundId;
    private Long instrumentId;

    public FundOwnership toFundOwnership() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, FundOwnership.class);
    }
}
