package org.fund.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.fund.accounting.detailLedger.DetailLedgerDto;
import org.fund.baseInformation.customer.dto.CustomerBankAccountDto;
import org.fund.baseInformation.financialInstitutionBankAccount.dto.FinancialInstitutionResponseDto;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "AHA_DETAIL_LEDGER")
@Entity(name = "detailLedger")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailLedger extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    @Column(columnDefinition = "NVARCHAR2(12)", name = "NAME", nullable = false)
    private String name;
    @Column(columnDefinition = "NVARCHAR2(12)", name = "CODE", nullable = false)
    private String code;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_DETAIL_LEDGER_TYPE_ID")
    private DetailLedgerType detailLedgerType;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;

    public DetailLedgerDto toDto() {
        ObjectMapper objectMapper = new ObjectMapper();
        DetailLedgerDto detailLedgerDto = objectMapper.convertValue(this, DetailLedgerDto.class);
        detailLedgerDto.setDetailLedgerTypeId(detailLedgerType.getId());
        return detailLedgerDto;
    }
}
