package org.fund.baseInformation.customer.dto;

import lombok.Getter;
import lombok.Setter;
import org.fund.authentication.user.dto.PersonDto;
import org.fund.model.CustomerStatus;
import org.fund.model.DetailLedger;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;

@Getter
@Setter
public class CustomerResponseDto {
    private Long id;
    @ValidateField(fieldName = "detailLedgerId", entityClass = DetailLedger.class)
    private Long detailLedgerId;
    private String dlNumber;
    @ValidateField(fieldName = "customerStatusId", entityClass = CustomerStatus.class)
    @NotEmpty(fieldName = "customerStatusId")
    private Long customerStatusId;
    private String customerStatusName;
    @ValidateField(fieldName = "customerBankAccountId", entityClass = CustomerBankAccountDto.class)
    private Long customerBankAccountId;
    private String customerBankName;
    private String accountNumber;
    private String shabaNumber;
    private PersonDto person;
    private String comments;
    private boolean isSmsSend;
    private boolean isSejam;
    private float profitRate;
    private boolean isProfitIssue;
    private boolean isVat;
    private boolean isEpaymentCustomer;
}
