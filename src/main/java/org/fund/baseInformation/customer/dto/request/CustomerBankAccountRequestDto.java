package org.fund.baseInformation.customer.dto.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.fund.baseInformation.bankAccount.BankAccountDto;
import org.fund.dto.DtoConvertible;
import org.fund.model.BankAccount;
import org.fund.model.Customer;
import org.fund.model.CustomerBankAccount;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;

import java.util.List;
@Getter
@Setter
public class CustomerBankAccountRequestDto  implements DtoConvertible  {
    private Long id;
    @NotEmpty(fieldName = "customerId")
    @ValidateField(fieldName = "customerId", entityClass = Customer.class)
    private Long customerId;
    private BankAccountDto bankAccount;

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        ObjectMapper objectMapper = new ObjectMapper();
        T entity = objectMapper.convertValue(this, targetType);

        if (entity instanceof CustomerBankAccount customerBankAccount) {
            customerBankAccount.setCustomer(getCustomer(repository, customerId));
            customerBankAccount.setBankAccount(getBankAccount(repository, bankAccount));
        }

        return entity;
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        return List.of();
    }

    private Customer getCustomer(JpaRepository repository, Long customerId) {
        return repository.findAll(Customer.class).stream()
                .filter(a -> a.getId().equals(customerId))
                .findFirst()
                .orElse(null);
    }

    private BankAccount getBankAccount(JpaRepository repository, BankAccountDto bankAccount) {
        return repository.findOne(BankAccount.class, bankAccount.getId());
    }
}

