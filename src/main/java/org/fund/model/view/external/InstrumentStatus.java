package org.fund.model.view.external;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.fund.config.cache.CacheableEntity;

@Table(name = "INSTRUMENT_STATUS")
@Entity(name = "instrumentStatus")
@Getter
@Setter
@Builder
@CacheableEntity
public class InstrumentStatus {
    @Column(columnDefinition = "NUMBER", name = "INSTRUMENT_STATUS_ID", nullable = false)
    private Long id;
    @Column(columnDefinition = "VARCHAR2(200)", name = "INSTRUMENT_STATUS_NAME", nullable = false)
    private String name;
}