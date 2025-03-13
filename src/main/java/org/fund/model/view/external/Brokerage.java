package org.fund.model.view.external;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.fund.config.cache.CacheableEntity;

@Table(name = "BROKERAGE")
@Entity(name = "brokerage")
@Getter
@Setter
@CacheableEntity
@AllArgsConstructor
@NoArgsConstructor
public class Brokerage {
    @Id
    @Column(columnDefinition = "NUMBER", name = "BROKERAGE_ID", nullable = false)
    private Long id;
    @Column(columnDefinition = "VARCHAR2(50)", name = "BROKERAGE_CODE", nullable = false)
    private String code;
    @Column(columnDefinition = "VARCHAR2(200)", name = "BROKERAGE_NAME", nullable = false)
    private String name;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;



}