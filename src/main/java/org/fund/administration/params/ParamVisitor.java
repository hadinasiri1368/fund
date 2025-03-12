package org.fund.administration.params;

import org.fund.model.DetailLedger;
import org.fund.model.Fund;
import org.fund.model.Params;
import org.fund.model.SubsidiaryLedger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParamVisitor {
    private final List<Param> params;

    public ParamVisitor() {
        params = new ArrayList<Param>();
    }

    @Transactional
    public void delete(Long paramId, Long userId, String uuid) throws Exception {
        getParam().delete(paramId, userId, uuid);
    }

    @Transactional
    public void update(Params param, Long userId, String uuid) throws Exception {
        getParam().update(param, userId, uuid);
    }

    @Transactional
    public void insert(Params param, Long userId, String uuid) throws Exception {
        getParam().insert(param, userId, uuid);
    }

    public Params getParam(Fund fund, String code) {
        return getParam().getParam(fund, code);
    }

    // ------------------------------------------------------------------
    public String getStringValue(String paramCode) {
        return getParam().getStringValue(paramCode);
    }

    public Long getLongValue(String paramCode) {
        return getParam().getLongValue(paramCode);
    }

    public Double getDoubleValue(String paramCode) {
        return getParam().getDoubleValue(paramCode);
    }

    public Float getFloatValue(String paramCode) {
        return getParam().getFloatValue(paramCode);
    }

    public Boolean getBooleanValue(String paramCode) {
        return getParam().getBooleanValue(paramCode);
    }

// ------------------------------------------------------------------

    public String getStringValue(Fund fund, String paramCode) {
        return getParam().getStringValue(fund, paramCode);
    }

    public Long getLongValue(Fund fund, String paramCode) {
        return getParam().getLongValue(fund, paramCode);
    }

    public Double getDoubleValue(Fund fund, String paramCode) {
        return getParam().getDoubleValue(fund, paramCode);
    }

    public Float getFloatValue(Fund fund, String paramCode) {
        return getParam().getFloatValue(fund, paramCode);
    }

    public Boolean getBooleanValue(Fund fund, String paramCode) {
        return getParam().getBooleanValue(fund, paramCode);
    }

    // ------------------------------------------------------------------

    public String getStringValue(Fund fund, String paramCode, String effectiveDate) {
        return getParam().getStringValue(fund, paramCode, effectiveDate);
    }

    public Long getLongValue(Fund fund, String paramCode, String effectiveDate) {
        return getParam().getLongValue(fund, paramCode, effectiveDate);
    }

    public Double getDoubleValue(Fund fund, String paramCode, String effectiveDate) {
        return getParam().getDoubleValue(fund, paramCode, effectiveDate);
    }

    public Float getFloatValue(Fund fund, String paramCode, String effectiveDate) {
        return getParam().getFloatValue(fund, paramCode, effectiveDate);
    }

    public Boolean getBooleanValue(Fund fund, String paramCode, String effectiveDate) {
        return getParam().getBooleanValue(fund, paramCode, effectiveDate);
    }

    // ------------------------------------------------------------------

    public String getStringValue(String paramCode, String effectiveDate) {
        return getParam().getStringValue(paramCode, effectiveDate);
    }

    public Boolean getBooleanValue(String paramCode, String effectiveDate) {
        return getParam().getBooleanValue(paramCode, effectiveDate);
    }

    public Long getLongValue(String paramCode, String effectiveDate) {
        return getParam().getLongValue(paramCode, effectiveDate);
    }

    public Double getDoubleValue(String paramCode, String effectiveDate) {
        return getParam().getDoubleValue(paramCode, effectiveDate);
    }

    public Float getFloatValue(String paramCode, String effectiveDate) {
        return getParam().getFloatValue(paramCode, effectiveDate);
    }

    // ------------------------------------------------------------------

    public SubsidiaryLedger getSubsidiaryLedger(String paramCode) {
        return getParam().getSubsidiaryLedger(paramCode);
    }

    public SubsidiaryLedger getSubsidiaryLedger(Fund fund, String paramCode) {
        return getParam().getSubsidiaryLedger(fund, paramCode);
    }

    // ------------------------------------------------------------------

    public DetailLedger getDetailLedger(String paramCode) {
        return getParam().getDetailLedger(paramCode);
    }

    DetailLedger getDetailLedger(Fund fund, String paramCode) {
        return getParam().getDetailLedger(fund, paramCode);
    }


    private Param getParam() {
        for (Param p : params) {
            if (p.accept()) {
                return p;
            }
        }
        return null;
    }
}
