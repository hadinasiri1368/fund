package org.fund.model.view.external;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.fund.config.cache.CacheableEntity;

@Table(name = "INST_TYPE_DERIVATIVES")
@Entity(name = "instrumentTypeDerivatives")
@Getter
@Setter
@Builder
@CacheableEntity
public class InstrumentTypeDerivatives {
    @Column(columnDefinition = "NUMBER", name = "INST_TYPE_DERIVATIVES_ID", nullable = false)
    private Long id;
    @Column(columnDefinition = "VARCHAR2(100)", name = "INST_TYPE_DERIVATIVES_NAME", nullable = false)
    private String name;
}