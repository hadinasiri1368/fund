package org.fund.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.fund.authentication.user.dto.PersonDto;
import org.fund.baseInformation.bankAccount.BankAccountDto;
import org.fund.config.cache.CacheableEntity;
import org.fund.model.view.external.Bank;

import java.io.Serializable;

@Table(name = "AHA_BANK_ACCOUNT")
@Entity(name = "bankAccount")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
public class BankAccount extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_BANK_ACCOUNT_TYPE_ID")
    private BankAccountType bankAccountType;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_BANK_ID")
    private Bank bank;
    @Column(columnDefinition = "NVARCHAR2(50)", name = "ACCOUNT_NUMBER", nullable = false)
    private String accountNumber;
    @Column(columnDefinition = "NUMBER", name = "ANNUAL_INTEREST", nullable = false)
    private Long annualinterest;
    @Column(columnDefinition = "NVARCHAR2(50)", name = "SHABA_NUMBER", nullable = false)
    private String shabaNumber;

    public BankAccountDto toDto() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, BankAccountDto.class);
    }
}
