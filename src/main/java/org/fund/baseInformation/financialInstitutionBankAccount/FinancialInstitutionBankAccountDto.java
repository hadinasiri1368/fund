package org.fund.baseInformation.financialInstitutionBankAccount;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.fund.baseInformation.bankAccount.BankAccountDto;
import org.fund.model.*;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class FinancialInstitutionBankAccountDto {
    private final JpaRepository repository;

    public FinancialInstitutionBankAccountDto(JpaRepository repository) {
        this.repository = repository;
    }

    private Long id;
    @NotEmpty(fieldName = "name")
    private String name;
    private BankAccountDto bankAccount;
    @NotEmpty(fieldName = "detailLedgerTypeId")
    private Long detailLedgerTypeId;

    public FinancialInstitutionBankAccount toFinancialInstitutionBankAccount() {
        ObjectMapper objectMapper = new ObjectMapper();
        FinancialInstitutionBankAccount financialInstitutionBankAccount = objectMapper.convertValue(this, FinancialInstitutionBankAccount.class);
        financialInstitutionBankAccount.setBankAccount(getBankAccount(bankAccount));
        return financialInstitutionBankAccount;
    }

    private BankAccount getBankAccount(BankAccountDto bankAccount) {
        return bankAccount.toBankAccount();
    }

}
