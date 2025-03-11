package org.fund.model.view.external;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.fund.config.cache.CacheableEntity;
import org.fund.model.DetailLedgerType;

@Table(name = "INSTRUMENT")
@Entity(name = "instrument")
@Getter
@Setter
@Builder
@CacheableEntity
public class Instrument {
    @Column(columnDefinition = "NUMBER", name = "INSTRUMENT_ID", nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "INST_TYPE_DERIVATIVES_ID")
    private InstrumentTypeDerivatives instrumentTypeDerivatives;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "INDUSTRY_ID")
    private Industry industry;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "INSTRUMENT_STATUS_ID")
    private InstrumentStatus instrumentStatus;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "INSTRUMENT_TYPE_ID")
    private InstrumentType instrumentType;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;
    @Column(columnDefinition = "VARCHAR2(500)", name = "FARSI_NAME", nullable = false)
    private String name;
    @Column(columnDefinition = "VARCHAR2(300)", name = "FARSI_ABBREVIATION", nullable = false)
    private String abbreviation;
    @Column(columnDefinition = "VARCHAR2(100)", name = "BOURSE_ACCOUNT", nullable = false)
    private String bourseAccount;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_OTC", nullable = false)
    private Boolean isOtc;
}