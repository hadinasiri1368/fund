package org.fund.model;

import jakarta.persistence.*;
import lombok.*;
import org.fund.config.cache.CacheableEntity;

import java.io.Serializable;

@Table(name = "AHA_PARAMS")
@Entity(name = "params")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CacheableEntity
public class Params extends BaseEntity implements Serializable {
    public Params(Params params){
        this.setId(params.getId());
        this.setName(params.getName());
        this.setValue(params.getValue());
        this.setDataQuery(params.getDataQuery());
        this.setIsActive(params.getIsActive());
        this.setIsEditable(params.getIsEditable());
        this.setParamsType(params.getParamsType());
        this.setFund(params.getFund());
        this.setDetailLedger(params.getDetailLedger());
        this.setSubsidiaryLedger(params.getSubsidiaryLedger());
        this.setParamsValueType(params.getParamsValueType());
        this.setIsGlobal(params.getIsGlobal());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    @Column(columnDefinition = "NVARCHAR2(12)", name = "NAME", nullable = false)
    private String name;
    @Column(columnDefinition = "NVARCHAR2(12)", name = "CODE", nullable = false)
    private String code;
    @Column(columnDefinition = "VARCHAR2(400)", name = "VALUE", nullable = false)
    private String value;
    @Column(columnDefinition = "VARCHAR2(400)", name = "DATA_QUERY", nullable = false)
    private String dataQuery;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_EDITABLE", nullable = false)
    private Boolean isEditable;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_PARAMS_TYPE_ID")
    private ParamsType paramsType;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_FUND_ID")
    private Fund fund;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_DETAIL_LEDGER_ID")
    private DetailLedger detailLedger;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_SUBSIDIARY_LEDGER_ID")
    private SubsidiaryLedger subsidiaryLedger;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "F_PARAMS_VALUE_TYPE_ID")
    private ParamsValueType paramsValueType;
    @Column(columnDefinition = "NUMBER(1)", name = "IS_GLOBAL", nullable = false)
    private Boolean isGlobal;

}
