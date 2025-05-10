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

@Table(name = "bourse_fund")
@Entity(name = "bourseFund")
@Getter
@Setter
@CacheableEntity
@AllArgsConstructor
@NoArgsConstructor
public class BourseFund {
    @Id
    @Column(columnDefinition = "NUMBER", name = "BOURSE_FUND_ID", nullable = false)
    private Long id;
    @Column(columnDefinition = "VARCHAR2(200)", name = "BOURSE_FUND_NAME", nullable = false)
    private String name;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;
}