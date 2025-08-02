package org.fund.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.fund.authentication.user.dto.PersonDto;
import org.fund.baseInformation.financialInstitutionBankAccount.dto.FinancialInstitutionRequestDto;
import org.fund.baseInformation.financialInstitutionBankAccount.dto.FinancialInstitutionResponseDto;
import org.fund.config.cache.CacheableEntity;

@Table(name = "AHA_FINANCIAL_INSTITUTION")
@Entity(name = "financialInstitution")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@CacheableEntity
public class FinancialInstitution {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    @Column(columnDefinition = "NVARCHAR2(200)", name = "NAME", nullable = false)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_DETAIL_LEDGER_ID")
    private DetailLedger detailLedger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_PERSON_ID")
    private Person person;

    public FinancialInstitutionResponseDto toDto() {
        ObjectMapper objectMapper = new ObjectMapper();
        FinancialInstitutionResponseDto financialInstitutionResponseDto = objectMapper.convertValue(this, FinancialInstitutionResponseDto.class);
        financialInstitutionResponseDto.setPerson(person.toDto());
        financialInstitutionResponseDto.setDetailLedgerDto(detailLedger.toDto());
        return financialInstitutionResponseDto;
    }
}
