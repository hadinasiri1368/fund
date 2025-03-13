package org.fund.model.view.external;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.fund.config.cache.CacheableEntity;

@Table(name = "INSTRUMENT_INFO")
@Entity(name = "instrumentInfo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@CacheableEntity
public class InstrumentInfo {
    @Id
    @Column(columnDefinition = "NUMBER", name = "INSTRUMENT_ID", nullable = false)
    private Long instrumentId;
    @Column(columnDefinition = "VARCHAR2(12)", name = "INS_MAX_LCODE", nullable = false)
    private String insMaxLcode;
    @Column(columnDefinition = "VARCHAR2(20)", name = "INS_MIN_LCODE", nullable = false)
    private String insMinLcode;
    @Column(columnDefinition = "VARCHAR2(48)", name = "ISIN_CODE", nullable = false)
    private String isinCode;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_RIGHT", nullable = false)
    private Boolean isRight;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_OTC", nullable = false)
    private Boolean isOtc;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_FOR_FUND_XML", nullable = false)
    private Boolean isForFundXml;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_VALID", nullable = false)
    private Boolean isValid;

}