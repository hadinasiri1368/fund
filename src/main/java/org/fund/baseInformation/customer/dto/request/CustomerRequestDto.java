package org.fund.baseInformation.customer.dto.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.fund.authentication.user.dto.PersonDto;
import org.fund.baseInformation.customer.dto.CustomerBAnkAccountDto;
import org.fund.common.FundUtils;
import org.fund.dto.DtoConvertible;
import org.fund.model.*;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;

import java.util.List;

@Getter
@Setter
public class CustomerRequestDto implements DtoConvertible {
    private Long id;
    @ValidateField(fieldName = "detailLedgerId", entityClass = DetailLedger.class)
    private Long detailLedgerId;
    @ValidateField(fieldName = "customerStatusId", entityClass = CustomerStatus.class)
    @NotEmpty(fieldName = "customerStatusId")
    private Long customerStatusId;
    @ValidateField(fieldName = "customerBankAccountId", entityClass = CustomerBAnkAccountDto.class)
    private Long customerBankAccountId;
    private PersonDto person;
    private String comments;
    private boolean isSmsSend;
    private boolean isSejam;
    private float profitRate;
    private boolean isProfitIssue;
    private boolean isVat;
    private boolean isEpaymentCustomer;

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        ObjectMapper objectMapper = new ObjectMapper();
        T entity = objectMapper.convertValue(this, targetType);

        if (entity instanceof Customer customer) {
            customer.setDetailLedger(getDetailLedger(repository, detailLedgerId));
            customer.setCustomerStatus(getCustomerStatus(repository, customerStatusId));
            customer.setPerson(getPerson(repository, person));
            customer.setCustomerBankAccount(getCustomerBankAccount(repository, customerBankAccountId));
        }

        return entity;
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        return List.of();
    }

    private DetailLedger getDetailLedger(JpaRepository repository, Long detailLedgerId) {
        return repository.findAll(DetailLedger.class).stream()
                .filter(a -> a.getId().equals(detailLedgerId))
                .findFirst()
                .orElse(null);
    }

    private CustomerStatus getCustomerStatus(JpaRepository repository, Long customerStatusId) {
        return repository.findAll(CustomerStatus.class).stream()
                .filter(a -> a.getId().equals(customerStatusId))
                .findFirst()
                .orElse(null);
    }

    private Person getPerson(JpaRepository repository, PersonDto person) {
        if (!FundUtils.isNull(person.getId()))
            return repository.findAll(Person.class).stream()
                    .filter(a -> a.getId().equals(person.getId()))
                    .findFirst()
                    .orElse(null);
        return repository.findOne(Person.class, person.getId());
    }

    private CustomerBankAccount getCustomerBankAccount(JpaRepository repository, Long customerBankAccountId) {
        return repository.findAll(CustomerBankAccount.class).stream()
                .filter(a -> a.getId().equals(customerBankAccountId))
                .findFirst()
                .orElse(null);
    }
}
