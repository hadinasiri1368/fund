package org.fund.params;

import org.fund.common.FundUtils;
import org.fund.exception.FundException;
import org.fund.exception.GeneralExceptionType;
import org.fund.exception.ParamExceptionType;
import org.fund.model.DetailLedger;
import org.fund.model.Fund;
import org.fund.model.Params;
import org.fund.model.SubsidiaryLedger;
import org.springframework.stereotype.Service;

@Service
public class ParamService {
    private final ParamVisitor paramVisitor;

    public ParamService(ParamVisitor paramVisitor) {
        this.paramVisitor = paramVisitor;
    }

    public void insert(ParamDto param, Long userId, String uuid) throws Exception {
        paramVisitor.insert(param.toParams(), userId, uuid);
    }

    public void delete(Long paramId, Long userId, String uuid) throws Exception {
        paramVisitor.delete(paramId, userId, uuid);
    }

    public void setValue(Fund fund, String code, String value, Long userId, String uuid) throws Exception {
        Params param = paramVisitor.getParam(fund, code);
        if (FundUtils.isNull(param))
            throw new FundException(ParamExceptionType.PARAM_NOT_FOUND, new Object[]{code});
        param.setValue(value);
        paramVisitor.update(param, userId, uuid);
    }

    public String getStringValue(Fund fund, String paramCode) {
        return paramVisitor.getStringValue(fund, paramCode);
    }

    public Long getLongValue(Fund fund, String paramCode) {
        return paramVisitor.getLongValue(fund, paramCode);
    }

    public Double getDoubleValue(Fund fund, String paramCode) {
        return paramVisitor.getDoubleValue(fund, paramCode);
    }

    public Float getFloatValue(Fund fund, String paramCode) {
        return paramVisitor.getFloatValue(fund, paramCode);
    }

    public Boolean getBooleanValue(Fund fund, String paramCode) {
        return paramVisitor.getBooleanValue(fund, paramCode);
    }


    public String getStringValue(String paramCode) {
        return paramVisitor.getStringValue(paramCode);
    }

    public Long getLongValue(String paramCode) {
        return paramVisitor.getLongValue(paramCode);
    }

    public Double getDoubleValue(String paramCode) {
        return paramVisitor.getDoubleValue(paramCode);
    }

    public Float getFloatValue(String paramCode) {
        return paramVisitor.getFloatValue(paramCode);
    }

    public Boolean getBooleanValue(String paramCode) {
        return paramVisitor.getBooleanValue(paramCode);
    }


    public String getStringValue(String paramCode, String effectiveDate) {
        return paramVisitor.getStringValue(paramCode, effectiveDate);
    }

    public Long getLongValue(String paramCode, String effectiveDate) {
        return paramVisitor.getLongValue(paramCode, effectiveDate);
    }

    public Double getDoubleValue(String paramCode, String effectiveDate) {
        return paramVisitor.getDoubleValue(paramCode, effectiveDate);
    }

    public Float getFloatValue(String paramCode, String effectiveDate) {
        return paramVisitor.getFloatValue(paramCode, effectiveDate);
    }

    public Boolean getBooleanValue(String paramCode, String effectiveDate) {
        return paramVisitor.getBooleanValue(paramCode, effectiveDate);
    }


    public String getStringValue(Fund fund, String paramCode, String effectiveDate) {
        return paramVisitor.getStringValue(fund, paramCode, effectiveDate);
    }

    public Long getLongValue(Fund fund, String paramCode, String effectiveDate) {
        return paramVisitor.getLongValue(fund, paramCode, effectiveDate);
    }

    public Double getDoubleValue(Fund fund, String paramCode, String effectiveDate) {
        return paramVisitor.getDoubleValue(fund, paramCode, effectiveDate);
    }

    public Float getFloatValue(Fund fund, String paramCode, String effectiveDate) {
        return paramVisitor.getFloatValue(fund, paramCode, effectiveDate);
    }

    public Boolean getBooleanValue(Fund fund, String paramCode, String effectiveDate) {
        return paramVisitor.getBooleanValue(fund, paramCode, effectiveDate);
    }


    public SubsidiaryLedger getSubsidiaryLedger(String paramCode) {
        return paramVisitor.getSubsidiaryLedger(paramCode);
    }

    public SubsidiaryLedger getSubsidiaryLedger(Fund fund, String paramCode) {
        return paramVisitor.getSubsidiaryLedger(fund, paramCode);
    }


    public DetailLedger getDetailLedger(String paramCode) {
        return paramVisitor.getDetailLedger(paramCode);
    }

    public DetailLedger getDetailLedger(Fund fund, String paramCode) {
        return paramVisitor.getDetailLedger(fund, paramCode);
    }


}
