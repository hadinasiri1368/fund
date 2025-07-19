package org.fund.baseInformation.customer.dto;

import lombok.Getter;
import lombok.Setter;
import org.fund.authentication.user.dto.PersonDto;

@Getter
@Setter
public class CustomerResponseDto {
    private Long id;
    private Long detailLedgerId;
    private String dlNumber;
    private Long customerStatusId;
    private String customerStatusName;
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
