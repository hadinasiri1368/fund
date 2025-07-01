package org.fund.baseInformation.financialInstitutionBankAccount;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.fund.baseInformation.bankAccount.BankAccountDto;
import org.fund.dto.DtoConvertible;
import org.fund.model.*;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
public class FinancialInstitutionBankAccountDto implements DtoConvertible {
    private Long id;
    @NotEmpty(fieldName = "name")
    private String name;
    private BankAccountDto bankAccount;
    @NotEmpty(fieldName = "detailLedgerTypeId")
    private Long detailLedgerTypeId;

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        ObjectMapper objectMapper = new ObjectMapper();
        T entity = objectMapper.convertValue(this, targetType);

        if (entity instanceof FinancialInstitutionBankAccount financialInstitutionBankAccount) {
            financialInstitutionBankAccount.setBankAccount(getBankAccount(repository, bankAccount));
        }

        return entity;
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        return List.of();
    }

    private BankAccount getBankAccount(JpaRepository repository, BankAccountDto bankAccount) {
        return repository.findOne(BankAccount.class, bankAccount.getId());
    }

}
