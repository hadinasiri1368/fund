package org.fund.administration.verificationCode;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.fund.model.FundBranch;
import org.fund.model.VerificationCode;
import org.fund.validator.NotEmpty;

@Getter
@Setter
@Builder
public class VerificationCodeDto {
    private Long id;
    @NotEmpty(fieldName = "seed")
    private String seed;
    @NotEmpty(fieldName = "counter")
    private Long counter;
    @NotEmpty(fieldName = "isActive")
    private Boolean isActive;

    public VerificationCode toVerificationCode() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, VerificationCode.class);
    }
}
