package org.fund.administration.params;

import org.fund.common.DateUtils;
import org.fund.common.FundUtils;
import org.fund.constant.Consts;
import org.fund.exception.FundException;
import org.fund.exception.ParamExceptionType;
import org.fund.model.*;
import org.fund.administration.params.constant.ParamType;
import org.fund.administration.params.constant.ParamValueType;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

@Service
public abstract class ParamAbstract implements Param {
    protected final JpaRepository repository;

    public ParamAbstract(JpaRepository repository) {
        this.repository = repository;
    }

    public abstract void validateBeforInsert(Params param);

    public abstract void validateBeforUpdate(Params param, Params oldParam);

    @Override
    public String getStringValue(String paramCode) {
        Params param = getParams(paramCode, ParamValueType.STRING);
        if (FundUtils.isNull(param))
            return null;
        return param.getValue();
    }

    @Override
    public Long getLongValue(String paramCode) {
        Params param = getParams(paramCode, ParamValueType.NUMBER);
        if (FundUtils.isNull(param))
            return null;
        return FundUtils.longValue(param.getValue());
    }

    @Override
    public Double getDoubleValue(String paramCode) {
        Params param = getParams(paramCode, ParamValueType.NUMBER);
        if (FundUtils.isNull(param))
            return null;
        return FundUtils.doubleValue(param.getValue());
    }

    @Override
    public Float getFloatValue(String paramCode) {
        Params param = getParams(paramCode, ParamValueType.NUMBER);
        if (FundUtils.isNull(param))
            return null;
        return FundUtils.floatValue(param.getValue());
    }

    @Override
    public Boolean getBooleanValue(String paramCode) {
        Params param = getParams(paramCode, ParamValueType.BOOLEAN);
        if (FundUtils.isNull(param))
            return null;
        return FundUtils.booleanValue(param.getValue());
    }


    @Override
    public String getStringValue(Fund fund, String paramCode) {
        Params param = getParams(fund, paramCode, ParamValueType.STRING);
        if (FundUtils.isNull(param))
            return null;
        return param.getValue();
    }

    @Override
    public Long getLongValue(Fund fund, String paramCode) {
        Params param = getParams(fund, paramCode, ParamValueType.NUMBER);
        if (FundUtils.isNull(param))
            return null;
        return FundUtils.longValue(param.getValue());
    }

    @Override
    public Double getDoubleValue(Fund fund, String paramCode) {
        Params param = getParams(fund, paramCode, ParamValueType.NUMBER);
        if (FundUtils.isNull(param))
            return null;
        return FundUtils.doubleValue(param.getValue());
    }

    @Override
    public Float getFloatValue(Fund fund, String paramCode) {
        Params param = getParams(fund, paramCode, ParamValueType.NUMBER);
        if (FundUtils.isNull(param))
            return null;
        return FundUtils.floatValue(param.getValue());
    }

    @Override
    public Boolean getBooleanValue(Fund fund, String paramCode) {
        Params param = getParams(fund, paramCode, ParamValueType.BOOLEAN);
        if (FundUtils.isNull(param))
            return null;
        return FundUtils.booleanValue(param.getValue());
    }


    @Override
    public String getStringValue(String paramCode, String effectiveDate) {
        Params param = getParams(paramCode, ParamValueType.STRING, effectiveDate);
        if (FundUtils.isNull(param))
            return null;
        return param.getValue();
    }

    @Override
    public Long getLongValue(String paramCode, String effectiveDate) {
        Params param = getParams(paramCode, ParamValueType.NUMBER, effectiveDate);
        if (FundUtils.isNull(param))
            return null;
        return FundUtils.longValue(param.getValue());
    }

    @Override
    public Double getDoubleValue(String paramCode, String effectiveDate) {
        Params param = getParams(paramCode, ParamValueType.NUMBER, effectiveDate);
        if (FundUtils.isNull(param))
            return null;
        return FundUtils.doubleValue(param.getValue());
    }

    @Override
    public Float getFloatValue(String paramCode, String effectiveDate) {
        Params param = getParams(paramCode, ParamValueType.NUMBER, effectiveDate);
        if (FundUtils.isNull(param))
            return null;
        return FundUtils.floatValue(param.getValue());
    }

    @Override
    public Boolean getBooleanValue(String paramCode, String effectiveDate) {
        Params param = getParams(paramCode, ParamValueType.BOOLEAN, effectiveDate);
        if (FundUtils.isNull(param))
            return null;
        return FundUtils.booleanValue(param.getValue());
    }


    @Override
    public String getStringValue(Fund fund, String paramCode, String effectiveDate) {
        Params param = getParams(fund, paramCode, ParamValueType.STRING, effectiveDate);
        if (FundUtils.isNull(param))
            return null;
        return param.getValue();
    }

    @Override
    public Long getLongValue(Fund fund, String paramCode, String effectiveDate) {
        Params param = getParams(fund, paramCode, ParamValueType.NUMBER, effectiveDate);
        if (FundUtils.isNull(param))
            return null;
        return FundUtils.longValue(param.getValue());
    }

    @Override
    public Double getDoubleValue(Fund fund, String paramCode, String effectiveDate) {
        Params param = getParams(fund, paramCode, ParamValueType.NUMBER, effectiveDate);
        if (FundUtils.isNull(param))
            return null;
        return FundUtils.doubleValue(param.getValue());
    }

    @Override
    public Float getFloatValue(Fund fund, String paramCode, String effectiveDate) {
        Params param = getParams(fund, paramCode, ParamValueType.NUMBER, effectiveDate);
        if (FundUtils.isNull(param))
            return null;
        return FundUtils.floatValue(param.getValue());
    }

    @Override
    public Boolean getBooleanValue(Fund fund, String paramCode, String effectiveDate) {
        Params param = getParams(fund, paramCode, ParamValueType.BOOLEAN, effectiveDate);
        if (FundUtils.isNull(param))
            return null;
        return FundUtils.booleanValue(param.getValue());
    }

    protected Params getParams(Fund fund, String paramCode, ParamValueType paramsValueType) {
        Params param = repository.findAll(Params.class).stream()
                .filter(p -> (fund == null || p.getFund().equals(fund)) &&
                        !p.getParamsType().getId().equals(ParamType.ACCOUNT_SETUP.getId()) &&
                        p.getCode().equals(paramCode))
                .findFirst()
                .orElseThrow(() -> new FundException(ParamExceptionType.PARAM_NOT_FOUND, new Object[]{paramCode}));
        if (!FundUtils.isNull(param) && !param.getParamsValueType().getName().equals(paramsValueType.getTitle()))
            throw new FundException(ParamExceptionType.PARAM_TYPE_IS_NOT_EQUAL_OUTPUT_TYPE, new Object[]{param.getParamsValueType().getName()});
        return param;
    }

    protected Params getParams(String paramCode, ParamValueType paramsValueType) {
        Params param = repository.findAll(Params.class).stream()
                .filter(p -> p.getIsGlobal() &&
                        !p.getParamsType().getId().equals(ParamType.ACCOUNT_SETUP.getId()) &&
                        p.getCode().equals(paramCode))
                .findFirst()
                .orElseThrow(() -> new FundException(ParamExceptionType.PARAM_NOT_FOUND, new Object[]{paramCode}));
        if (!FundUtils.isNull(param) && !param.getParamsValueType().getName().equals(paramsValueType.getTitle()))
            throw new FundException(ParamExceptionType.PARAM_TYPE_IS_NOT_EQUAL_OUTPUT_TYPE, new Object[]{param.getParamsValueType().getName()});
        return param;
    }

    protected Params getParams(Fund fund, String paramCode, ParamValueType paramsValueType, String effectiveDate) {
        ParamsHistory paramsHistory = repository.findAll(ParamsHistory.class).stream()
                .filter(a -> a.getParams().getFund().equals(fund) &&
                        !a.getParams().getId().equals(ParamType.ACCOUNT_SETUP.getId()) &&
                        a.getParams().getCode().equals(paramCode) &&
                        a.getEffectiveDate().compareTo(effectiveDate) <= 0)
                .min(Comparator.comparing(ParamsHistory::getEffectiveDate))
                .orElseThrow(() -> new FundException(ParamExceptionType.PARAM_NOT_FOUND, new Object[]{paramCode}));
        if (!FundUtils.isNull(paramsHistory) && !paramsHistory.getParams().getParamsValueType().getName().equals(paramsValueType.getTitle()))
            throw new FundException(ParamExceptionType.PARAM_TYPE_IS_NOT_EQUAL_OUTPUT_TYPE, new Object[]{paramsHistory.getParams().getParamsValueType().getName()});
        Params params = new Params(paramsHistory.getParams());
        params.setValue(paramsHistory.getValue());
        return params;
    }

    protected Params getParams(String paramCode, ParamValueType paramsValueType, String effectiveDate) {
        ParamsHistory paramsHistory = repository.findAll(ParamsHistory.class).stream()
                .filter(a -> a.getParams().getIsGlobal() &&
                        !a.getParams().getId().equals(ParamType.ACCOUNT_SETUP.getId()) &&
                        a.getParams().getCode().equals(paramCode) &&
                        a.getEffectiveDate().compareTo(effectiveDate) <= 0)
                .min(Comparator.comparing(ParamsHistory::getEffectiveDate))
                .orElseThrow(() -> new FundException(ParamExceptionType.PARAM_NOT_FOUND, new Object[]{paramCode}));
        if (!FundUtils.isNull(paramsHistory) && !paramsHistory.getParams().getParamsValueType().getName().equals(paramsValueType.getTitle()))
            throw new FundException(ParamExceptionType.PARAM_TYPE_IS_NOT_EQUAL_OUTPUT_TYPE, new Object[]{paramsHistory.getParams().getParamsValueType().getName()});
        Params params = new Params(paramsHistory.getParams());
        params.setValue(paramsHistory.getValue());
        return params;
    }


    private String getEffectiveDate() {
        return DateUtils.getTodayJalali();
    }

    private void insertParamsHistory(String value, Long userId, String uuid) throws Exception {
        repository.save(ParamsHistory.builder()
                .effectiveDate(getEffectiveDate())
                .value(value)
                .build(), userId, uuid);
    }

    public void insert(Params params, Long userId, String uuid) throws Exception {
        validateBeforInsert(params);
        repository.save(params, userId, uuid);
        insertParamsHistory(params.getValue(), userId, uuid);

    }

    public void update(Params params, Long userId, String uuid) throws Exception {
        Params oldParam = repository.findOne(Params.class, params.getId());
        validateBeforUpdate(params, oldParam);
        repository.update(params, userId, uuid);
        if (!oldParam.getValue().equals(params.getValue()))
            insertParamsHistory(params.getValue(), userId, uuid);
    }

    public void delete(Long paramId, Long userId, String uuid) throws Exception {
        List<ParamsHistory> paramsHistories = repository.findAll(ParamsHistory.class).stream()
                .filter(a -> a.getParams().getId().equals(paramId))
                .toList();
        repository.batchRemove(paramsHistories, userId, uuid);
        repository.removeById(Params.class, paramId, userId, uuid);
    }

    public Params getParam(Fund fund, String code) {
        return repository.findAll(Params.class).stream()
                .filter(a -> a.getFund().equals(fund) && a.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

    protected void validateType(String value, ParamValueType paramValueType, ParamType paramType) {
        if (FundUtils.isNull(value) || paramType.getId().equals(ParamType.ACCOUNT_SETUP.getId()))
            return;
        boolean flag = true;
        try {
            switch (paramValueType) {
                case NUMBER:
                    Double.parseDouble(value);
                    break;
                case BOOLEAN:
                    flag = "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
                    break;
                case DATE:
                    Pattern datePattern = Pattern.compile(Consts.PERSIAN_DATE_REGEX);
                    flag = datePattern.matcher(value).matches() && DateUtils.isValid(value);
                    break;
                case DROPDOWN:
                    Integer.parseInt(value);
                    break;
                case TIME:
                    Pattern timePattern = Pattern.compile(Consts.TIME_REGEX);
                    flag = timePattern.matcher(value).matches();
                    break;
            }
        } catch (Exception e) {
            throw new FundException(ParamExceptionType.PARAM_VALUE_TYPE_IS_NOT_EQUAL_PARAMS_VALUE_TYPE, new Object[]{paramValueType.getTitle()});
        }
        if (!flag)
            throw new FundException(ParamExceptionType.PARAM_VALUE_TYPE_IS_NOT_EQUAL_PARAMS_VALUE_TYPE, new Object[]{paramValueType.getTitle()});
    }

    protected SubsidiaryLedger getSubsidiaryLedgerValue(Fund fund, String paramCode) {
        Params param = repository.findAll(Params.class).stream()
                .filter(a -> a.getCode().equals(paramCode) &&
                        (FundUtils.isNull(fund) || fund.equals(a.getFund())) &&
                        a.getIsGlobal() &&
                        a.getParamsType().getId().equals(ParamType.ACCOUNT_SETUP.getId()))
                .findFirst()
                .orElse(null);
        if (!FundUtils.isNull(param) && FundUtils.isNull(param.getSubsidiaryLedger()))
            throw new FundException(ParamExceptionType.PARAM_SUBSIDIARY_LEDGER_IS_NULL, new Object[]{paramCode});
        return param.getSubsidiaryLedger();
    }

    protected DetailLedger getDetailLedgerValue(Fund fund, String paramCode) {
        Params param = repository.findAll(Params.class).stream()
                .filter(a -> a.getCode().equals(paramCode) &&
                        (FundUtils.isNull(fund) || fund.equals(a.getFund())) &&
                        a.getIsGlobal() &&
                        a.getParamsType().getId().equals(ParamType.ACCOUNT_SETUP.getId()))
                .findFirst()
                .orElse(null);
        if (!FundUtils.isNull(param) && FundUtils.isNull(param.getDetailLedger()))
            throw new FundException(ParamExceptionType.PARAM_DETAIL_LEDGER_IS_NULL, new Object[]{paramCode});
        return param.getDetailLedger();
    }

    @Override
    public SubsidiaryLedger getSubsidiaryLedger(String paramCode) {
        return getSubsidiaryLedgerValue(null, paramCode);
    }

    @Override
    public SubsidiaryLedger getSubsidiaryLedger(Fund fund, String paramCode) {
        return getSubsidiaryLedgerValue(fund, paramCode);
    }


    @Override
    public DetailLedger getDetailLedger(String paramCode) {
        return getDetailLedgerValue(null, paramCode);
    }

    @Override
    public DetailLedger getDetailLedger(Fund fund, String paramCode) {
        return getDetailLedgerValue(fund, paramCode);
    }
}
