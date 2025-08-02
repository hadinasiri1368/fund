package org.fund.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.fund.baseInformation.customer.dto.CustomerBankAccountDto;
import org.fund.baseInformation.customer.dto.request.CustomerRequestDto;
import org.fund.common.FundUtils;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "AHA_CUSTOMER_BANK_ACCOUNT")
@Entity(name = "CustomerBankAccount")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@JsonIgnoreProperties(ignoreUnknown = true)
//@CacheableEntity
public class CustomerBankAccount extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_CUSTOMER_ID")
    private Customer customer;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_BANK_ACCOUNT_ID")
    private BankAccount bankAccount;

    public CustomerBankAccountDto toDto() {
        ObjectMapper objectMapper = new ObjectMapper();
        CustomerBankAccountDto customerBankAccountDto = objectMapper.convertValue(this, CustomerBankAccountDto.class);
        customerBankAccountDto.setCustomerId(customer.getId());
        customerBankAccountDto.setBankAccount(bankAccount.toDto());
        return customerBankAccountDto;
    }
}
