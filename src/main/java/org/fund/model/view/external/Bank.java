package org.fund.model.view.external;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fund.config.cache.CacheableEntity;

@Table(name = "BANK")
@Entity(name = "bank")
@Getter
@Setter
@CacheableEntity
@AllArgsConstructor
@NoArgsConstructor
public class Bank {
    @Id
    @Column(columnDefinition = "NUMBER", name = "BANK_ID", nullable = false)
    private Long id;
    @Column(columnDefinition = "VARCHAR2(50)", name = "BANK_NAME", nullable = false)
    private String name;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_VALID", nullable = false)
    private Boolean isValid;
}