package org.fund.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.fund.baseInformation.bankAccount.BankAccountTypeDto;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "AHA_BANK_ACCOUNT_TYPE")
@Entity(name = "bankAccountType")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
public class BankAccountType extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    @Column(columnDefinition = "NVARCHAR2(100)", name = "NAME", nullable = false)
    private String name;

    public BankAccountTypeDto toDto() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, BankAccountTypeDto.class);
    }
}
