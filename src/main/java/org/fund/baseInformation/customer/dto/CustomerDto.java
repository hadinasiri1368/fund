package org.fund.baseInformation.customer.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.fund.common.FundUtils;
import org.fund.model.*;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class CustomerDto {
    private final JpaRepository repository;

    public CustomerDto(JpaRepository repository) {
        this.repository = repository;
    }

    private Long id;
    @ValidateField(fieldName = "detailLedgerId", entityClass = DetailLedger.class)
    private Long detailLedgerId;
    @ValidateField(fieldName = "customerStatusId", entityClass = CustomerStatus.class)
    @NotEmpty(fieldName = "customerStatusId")
    private Long customerStatusId;
    @ValidateField(fieldName = "customerBankAccountId", entityClass = CustomerBankAccountDto.class)
    private Long customerBankAccountId;
    private PersonDto person;
    private String comments;
    private boolean isSmsSend;
    private boolean isSejam;
    private float profitRate;
    private boolean isProfitIssue;
    private boolean isVat;
    private boolean isEpaymentCustomer;


    public Customer toCustomer() {
        ObjectMapper objectMapper = new ObjectMapper();
        Customer customer = objectMapper.convertValue(this, Customer.class);
        customer.setDetailLedger(getDetailLedger(detailLedgerId));
        customer.setCustomerStatus(getCustomerStatus(customerStatusId));
        customer.setPerson(getPerson(person));
        customer.setCustomerBankAccount(getCustomerBankAccount(customerBankAccountId));
        return customer;
    }

    private DetailLedger getDetailLedger(Long detailLedgerId) {
        return repository.findAll(DetailLedger.class).stream()
                .filter(a -> a.getId().equals(detailLedgerId))
                .findFirst()
                .orElse(null);
    }

    private CustomerStatus getCustomerStatus(Long customerStatusId) {
        return repository.findAll(CustomerStatus.class).stream()
                .filter(a -> a.getId().equals(customerStatusId))
                .findFirst()
                .orElse(null);
    }

    private Person getPerson(PersonDto person) {
        if (!FundUtils.isNull(person.getId()))
            return repository.findAll(Person.class).stream()
                    .filter(a -> a.getId().equals(person.getId()))
                    .findFirst()
                    .orElse(null);
        return person.toPerson();
    }

    private CustomerBankAccount getCustomerBankAccount(Long customerBankAccountId) {
        return repository.findAll(CustomerBankAccount.class).stream()
                .filter(a -> a.getId().equals(customerBankAccountId))
                .findFirst()
                .orElse(null);
    }
}
