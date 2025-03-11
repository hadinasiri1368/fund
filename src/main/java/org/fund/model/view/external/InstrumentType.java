package org.fund.model.view.external;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.fund.config.cache.CacheableEntity;

@Table(name = "INSTRUMENT_TYPE")
@Entity(name = "instrumentType")
@Getter
@Setter
@Builder
@CacheableEntity
public class InstrumentType {
    @Column(columnDefinition = "NUMBER", name = "INSTRUMENT_TYPE_ID", nullable = false)
    private Long id;
    @Column(columnDefinition = "VARCHAR2(50)", name = "INSTRUMENT_TYPE_NAME", nullable = false)
    private String name;
}