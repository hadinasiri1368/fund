package org.fund.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.fund.baseInformation.customer.dto.request.CustomerRequestDto;
import org.fund.baseInformation.customer.dto.response.CustomerResponseDto;
import org.fund.common.FundUtils;

@Table(name = "AHA_CUSTOMER")
@Entity(name = "customer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@CacheableEntity
public class Customer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_DETAIL_LEDGER_ID")
    private DetailLedger detailLedger;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_CUSTOMER_STATUS_ID")
    private CustomerStatus customerStatus;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_PERSON_ID")
    private Person person;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_CUSTOMER_BANK_ACCOUNT_ID")
    private CustomerBankAccount customerBankAccount;
    @Column(columnDefinition = "NVARCHAR2(1000)", name = "COMMENTS")
    private String comments;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_SMS_SEND")
    private Boolean isSmsSend;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_SEJAM")
    private Boolean isSejam;
    @Column(columnDefinition = "NUMBER", name = "PROFIT_RATE")
    private float profitRate;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_PROFIT_ISSUE")
    private Boolean isProfitIssue;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_VAT")
    private Boolean isVat;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_EPAYMENT_CUSTOMER")
    private Boolean isEpaymentCustomer;

    public CustomerRequestDto toDto() {
        ObjectMapper objectMapper = new ObjectMapper();
        CustomerRequestDto customerRequestDto = objectMapper.convertValue(this, CustomerRequestDto.class);
        customerRequestDto.setDetailLedgerId(detailLedger.getId());
        customerRequestDto.setCustomerStatusId(customerStatus.getId());
        if (!FundUtils.isNull(customerBankAccount))
            customerRequestDto.setCustomerBankAccountId(customerBankAccount.getId());
        customerRequestDto.setPerson(person.toDto());
        return customerRequestDto;
    }

    public CustomerResponseDto toResponseDto() {
        ObjectMapper objectMapper = new ObjectMapper();
        CustomerResponseDto customerResponseDto = objectMapper.convertValue(this, CustomerResponseDto.class);
        customerResponseDto.setId(id);
        if (!FundUtils.isNull(detailLedger)) {
            customerResponseDto.setDetailLedger(detailLedger.toDto());
        }
        if (!FundUtils.isNull(customerStatus)) {
            customerResponseDto.setCustomerStatus(customerStatus.toDto());
        }
        if (!FundUtils.isNull(customerBankAccount)) {
            customerResponseDto.setBankAccount(customerBankAccount.getBankAccount().toDto());
        }
        if (!FundUtils.isNull(person)) {
            customerResponseDto.setPerson(person.toDto());
        }
        return customerResponseDto;
    }
}
