package org.fund.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.fund.authentication.user.dto.PersonDto;
import org.fund.baseInformation.financialInstitutionBankAccount.dto.FinancialInstitutionBankAccountDto;
import org.fund.config.cache.CacheableEntity;

@Table(name = "AHA_FINANCIAL_INSTITUTION_BANK_ACCOUNT")
@Entity(name = "financialInstitutionBankAccount")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
public class FinancialInstitutionBankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_FINANCIAL_INSTITUTION_ID")
    private FinancialInstitution financialInstitution;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_BANK_ACCOUNT_ID")
    private BankAccount bankAccount;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;

    public FinancialInstitutionBankAccountDto toDto() {
        ObjectMapper objectMapper = new ObjectMapper();
        FinancialInstitutionBankAccountDto financialInstitutionBankAccountDto = objectMapper.convertValue(this, FinancialInstitutionBankAccountDto.class);
        financialInstitutionBankAccountDto.setFinancialInstitutionId(financialInstitution.getId());
        financialInstitutionBankAccountDto.setBankAccount(bankAccount.toDto());
        return financialInstitutionBankAccountDto;
    }
}
