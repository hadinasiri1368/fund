package org.fund.baseInformation.bankAccount;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.fund.model.BankAccount;
import org.fund.model.BankAccountType;
import org.fund.model.view.external.Bank;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;

@Getter
@Setter
public class BankAccountDto {
    private Long id;
    @NotEmpty(fieldName = "isActive")
    private Boolean isActive;
    @NotEmpty(fieldName = "bankAccountTypeId")
    @ValidateField(fieldName = "bankAccountTypeId",entityClass = BankAccountType.class)
    private Long bankAccountTypeId;
    @NotEmpty(fieldName = "bankId")
    @ValidateField(fieldName = "bankId",entityClass = Bank.class)
    private Long bankId;
    @NotEmpty(fieldName = "accountNumber")
    private String accountNumber;
    private Long annualinterest;
    private String shabaNumber;

    public BankAccount toBankAccount() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, BankAccount.class);
    }
}
