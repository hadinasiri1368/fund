package org.fund.baseInformation.customer.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.fund.accounting.detailLedger.DetailLedgerDto;
import org.fund.authentication.user.dto.PersonDto;
import org.fund.baseInformation.bankAccount.BankAccountDto;
import org.fund.baseInformation.customer.dto.CustomerStatusDto;


@Getter
@Setter
public class CustomerResponseDto {
    private Long id;
    private DetailLedgerDto detailLedger;
    private CustomerStatusDto customerStatus;
    private PersonDto person;
    private BankAccountDto bankAccount;
    private String comments;
    private Boolean isSmsSend;
    private Boolean isSejam;
    private float profitRate;
    private Boolean isProfitIssue;
    private Boolean isVat;
    private Boolean isEpaymentCustomer;
}
