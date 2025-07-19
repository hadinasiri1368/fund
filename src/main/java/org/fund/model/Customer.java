package org.fund.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.fund.baseInformation.customer.dto.CustomerRequestDto;
import org.fund.baseInformation.customer.dto.CustomerResponseDto;
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
    private boolean isSmsSend;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_SEJAM")
    private boolean isSejam;
    @Column(columnDefinition = "NUMBER", name = "PROFIT_RATE")
    private float profitRate;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_PROFIT_ISSUE")
    private boolean isProfitIssue;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_VAT")
    private boolean isVat;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_EPAYMENT_CUSTOMER")
    private boolean isEpaymentCustomer;

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
            customerResponseDto.setDetailLedgerId(detailLedger.getId());
            customerResponseDto.setDlNumber(detailLedger.getCode());
        }
        if (!FundUtils.isNull(customerStatus)) {
            customerResponseDto.setCustomerStatusId(customerStatus.getId());
            customerResponseDto.setCustomerStatusName(customerStatus.getName());
        }
        if (!FundUtils.isNull(customerBankAccount)) {
            customerResponseDto.setCustomerBankAccountId(customerBankAccount.getId());
            customerResponseDto.setCustomerBankName(customerBankAccount.getBankAccount().getBank().getName());
            customerResponseDto.setAccountNumber(customerBankAccount.getBankAccount().getAccountNumber());
            customerResponseDto.setShabaNumber(customerBankAccount.getBankAccount().getShabaNumber());
        }
        if (!FundUtils.isNull(person)) {
            customerResponseDto.setPerson(person.toDto());
        }
        customerResponseDto.setComments(comments);
        customerResponseDto.setSmsSend(isSmsSend);
        customerResponseDto.setSejam(isSejam);
        customerResponseDto.setProfitRate(profitRate);
        customerResponseDto.setProfitIssue(isProfitIssue);
        customerResponseDto.setVat(isVat);
        customerResponseDto.setEpaymentCustomer(isEpaymentCustomer);
        return customerResponseDto;
    }
}
