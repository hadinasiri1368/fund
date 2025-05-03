package org.fund.baseInformation.financialInstitutionBankAccount;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
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
    @NotEmpty(fieldName = "bankAccountId")
    @ValidateField(fieldName = "bankAccountId", entityClass = BankAccount.class)
    private Long bankAccountId;
    @NotEmpty(fieldName = "detailLedgerId")
    @ValidateField(fieldName = "detailLedgerId", entityClass = DetailLedger.class)
    private Long detailLedgerId;

    public FinancialInstitutionBankAccount toFinancialInstitutionBankAccount() {
        ObjectMapper objectMapper = new ObjectMapper();
        FinancialInstitutionBankAccount financialInstitutionBankAccount = objectMapper.convertValue(this, FinancialInstitutionBankAccount.class);
        financialInstitutionBankAccount.setBankAccount(getBankAccount(bankAccountId));
        financialInstitutionBankAccount.setDetailLedger(getdetailLedgerId(detailLedgerId));
        return financialInstitutionBankAccount;
    }

    private BankAccount getBankAccount(Long bankAccountId) {
        return repository.findAll(BankAccount.class).stream()
                .filter(a -> a.getId().equals(bankAccountId))
                .findFirst()
                .orElse(null);
    }

    private DetailLedger getdetailLedgerId(Long detailLedgerId) {
        return repository.findAll(DetailLedger.class).stream()
                .filter(a -> a.getId().equals(detailLedgerId))
                .findFirst()
                .orElse(null);
    }
}
