package org.fund.baseInformation.financialInstitutionBankAccount.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.fund.baseInformation.bankAccount.BankAccountDto;
import org.fund.common.FundUtils;
import org.fund.dto.DtoConvertible;
import org.fund.model.*;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;

import java.util.List;

@Getter
@Setter
public class FinancialInstitutionBankAccountDto implements DtoConvertible {
    private Long id;
    @ValidateField(fieldName = "id", entityClass = FinancialInstitution.class)
    private Long financialInstitutionId;
    @ValidateField(fieldName = "id", entityClass = BankAccount.class)
    private BankAccountDto bankAccount;
    @NotEmpty(fieldName = "isActive")
    private Boolean isActive;

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        ObjectMapper objectMapper = new ObjectMapper();
        T entity = objectMapper.convertValue(this, targetType);

        if (entity instanceof FinancialInstitutionBankAccount financialInstitutionBankAccount) {
            if (!FundUtils.isNull(bankAccount) && !FundUtils.isNull(bankAccount.getId()))
                financialInstitutionBankAccount.setBankAccount(getBankAccount(repository, bankAccount.getId()));
        }

        return entity;
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        return List.of();
    }

    private BankAccount getBankAccount(JpaRepository repository, Long bankAccountId) {
        return repository.findOne(BankAccount.class, bankAccountId);
    }
}
