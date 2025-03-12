package org.fund.administration.branch;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.fund.model.Fund;
import org.fund.model.FundBranch;
import org.fund.validator.NotEmpty;

@Getter
@Setter
@Builder
public class FundBranchDto {
    private Long id;
    @NotEmpty(fieldName = "isActive")
    private Boolean isActive;
    @NotEmpty(fieldName = "code")
    private String code;
    @NotEmpty(fieldName = "name")
    private String name;
    private String manager;
    private String phone;
    private String fax;
    private String cellPhone;
    private String postalCode;
    private String address;

    public FundBranch toFundBranch() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, FundBranch.class);
    }
}
