package org.fund.baseInformation.customer.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.fund.baseInformation.bankAccount.BankAccountDto;
import org.fund.model.BankAccount;
import org.fund.model.Customer;
import org.fund.model.CustomerBankAccount;
import org.fund.model.DetailLedger;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class CustomerBankAccountDto {
    private final JpaRepository repository;
    public CustomerBankAccountDto(JpaRepository repository) {
        this.repository = repository;
    }

    private Long id;
    @NotEmpty(fieldName = "customerId")
    @ValidateField(fieldName = "customerId", entityClass = Customer.class)
    private Long customerId;
    private BankAccountDto bankAccount;

    public CustomerBankAccount toCustomerBankAccount() {
        ObjectMapper objectMapper = new ObjectMapper();
        CustomerBankAccount customerBankAccount = objectMapper.convertValue(this, CustomerBankAccount.class);
        customerBankAccount.setCustomer(getCustomer(customerId));
        customerBankAccount.setBankAccount(getBankAccount(bankAccount));
        return customerBankAccount;
    }

    private Customer getCustomer(Long customerId) {
        return repository.findAll(Customer.class).stream()
                .filter(a -> a.getId().equals(customerId))
                .findFirst()
                .orElse(null);
    }

    private BankAccount getBankAccount(BankAccountDto bankAccount) {
        return bankAccount.toBankAccount();
    }
}
