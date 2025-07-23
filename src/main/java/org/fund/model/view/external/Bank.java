package org.fund.model.view.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fund.baseInformation.bankAccount.BankDto;
import org.fund.baseInformation.customer.dto.response.CustomerResponseDto;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "BANK")
@Entity(name = "bank")
@Getter
@Setter
@CacheableEntity
@AllArgsConstructor
@NoArgsConstructor
public class Bank implements Serializable {
    @Id
    @Column(columnDefinition = "NUMBER", name = "BANK_ID", nullable = false)
    private Long id;
    @Column(columnDefinition = "VARCHAR2(50)", name = "BANK_NAME", nullable = false)
    private String name;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_VALID", nullable = false)
    private Boolean isValid;

    public BankDto toDto() {
        ObjectMapper objectMapper = new ObjectMapper();
        return  objectMapper.convertValue(this, BankDto.class);
    }
}