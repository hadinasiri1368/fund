package org.fund.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.fund.baseInformation.customer.dto.CustomerDto;
import org.fund.common.FundUtils;
import org.fund.config.cache.CacheableEntity;

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

    public CustomerDto toDto() {
        ObjectMapper objectMapper = new ObjectMapper();
        CustomerDto customerDto = objectMapper.convertValue(this, CustomerDto.class);
        customerDto.setDetailLedgerId(detailLedger.getId());
        customerDto.setCustomerStatusId(customerStatus.getId());
        if (!FundUtils.isNull(customerBankAccount))
            customerDto.setCustomerBankAccountId(customerBankAccount.getId());
        customerDto.setPerson(person.toDto());
        return customerDto;
    }
}
