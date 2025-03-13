package org.fund.model.view.external;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.fund.config.cache.CacheableEntity;

@Table(name = "INSTRUMENT_STATUS")
@Entity(name = "instrumentStatus")
@Getter
@Setter
@CacheableEntity
@AllArgsConstructor
@NoArgsConstructor
public class InstrumentStatus {
    @Id
    @Column(columnDefinition = "NUMBER", name = "INSTRUMENT_STATUS_ID", nullable = false)
    private Long id;
    @Column(columnDefinition = "VARCHAR2(200)", name = "INSTRUMENT_STATUS_NAME", nullable = false)
    private String name;
}