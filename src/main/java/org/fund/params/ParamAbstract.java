package org.fund.params;

import org.fund.common.DateUtils;
import org.fund.common.FundUtils;
import org.fund.constant.Consts;
import org.fund.exception.FundException;
import org.fund.exception.GeneralExceptionType;
import org.fund.model.Fund;
import org.fund.model.Params;
import org.fund.model.ParamsHistory;
import org.fund.repository.JpaRepository;
import org.springframework.data.redis.connection.ReactiveListCommands;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

@Service
public abstract class ParamAbstract implements Param {
    public final JpaRepository repository;

    public ParamAbstract(JpaRepository repository) {
        this.repository = repository;
    }

    public abstract void setIsGlobal(Params param);

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
                .filter(p -> (fund == null || p.getFund().equals(fund)) && p.getCode().equals(paramCode))
                .findFirst()
                .orElseThrow(() -> new FundException(GeneralExceptionType.PARAM_NOT_FOUND, new Object[]{paramCode}));
        if (!FundUtils.isNull(param) && !param.getParamsValueType().getName().equals(paramsValueType.getTitle()))
            throw new FundException(GeneralExceptionType.PARAM_TYPE_IS_NOT_EQUAL_OUTPUT_TYPE, new Object[]{param.getParamsValueType().getName()});
        return param;
    }

    protected Params getParams(String paramCode, ParamValueType paramsValueType) {
        Params param = repository.findAll(Params.class).stream()
                .filter(p -> p.getIsGlobal() && p.getCode().equals(paramCode))
                .findFirst()
                .orElseThrow(() -> new FundException(GeneralExceptionType.PARAM_NOT_FOUND, new Object[]{paramCode}));
        if (!FundUtils.isNull(param) && !param.getParamsValueType().getName().equals(paramsValueType.getTitle()))
            throw new FundException(GeneralExceptionType.PARAM_TYPE_IS_NOT_EQUAL_OUTPUT_TYPE, new Object[]{param.getParamsValueType().getName()});
        return param;
    }

    protected Params getParams(Fund fund, String paramCode, ParamValueType paramsValueType, String effectiveDate) {
        ParamsHistory paramsHistory = repository.findAll(ParamsHistory.class).stream()
                .filter(a -> a.getParams().getFund().equals(fund) &&
                        a.getParams().getCode().equals(paramCode) &&
                        a.getEffectiveDate().compareTo(effectiveDate) <= 0)
                .min(Comparator.comparing(ParamsHistory::getEffectiveDate))
                .orElseThrow(() -> new FundException(GeneralExceptionType.PARAM_NOT_FOUND, new Object[]{paramCode}));
        if (!FundUtils.isNull(paramsHistory) && !paramsHistory.getParams().getParamsValueType().getName().equals(paramsValueType.getTitle()))
            throw new FundException(GeneralExceptionType.PARAM_TYPE_IS_NOT_EQUAL_OUTPUT_TYPE, new Object[]{paramsHistory.getParams().getParamsValueType().getName()});
        Params params = new Params(paramsHistory.getParams());
        params.setValue(paramsHistory.getValue());
        return params;
    }

    protected Params getParams(String paramCode, ParamValueType paramsValueType, String effectiveDate) {
        ParamsHistory paramsHistory = repository.findAll(ParamsHistory.class).stream()
                .filter(a -> a.getParams().getIsGlobal() &&
                        a.getParams().getCode().equals(paramCode) &&
                        a.getEffectiveDate().compareTo(effectiveDate) <= 0)
                .min(Comparator.comparing(ParamsHistory::getEffectiveDate))
                .orElseThrow(() -> new FundException(GeneralExceptionType.PARAM_NOT_FOUND, new Object[]{paramCode}));
        if (!FundUtils.isNull(paramsHistory) && !paramsHistory.getParams().getParamsValueType().getName().equals(paramsValueType.getTitle()))
            throw new FundException(GeneralExceptionType.PARAM_TYPE_IS_NOT_EQUAL_OUTPUT_TYPE, new Object[]{paramsHistory.getParams().getParamsValueType().getName()});
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
        setIsGlobal(params);
        validateType(params.getValue(), ParamValueType.getItemById(params.getParamsValueType().getId().intValue()));
        repository.save(params, userId, uuid);
        insertParamsHistory(params.getValue(), userId, uuid);

    }

    public void update(Params params, Long userId, String uuid) throws Exception {
        setIsGlobal(params);
        validateType(params.getValue(), ParamValueType.getItemById(params.getParamsValueType().getId().intValue()));
        repository.update(params, userId, uuid);
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

    private void validateType(String value, ParamValueType paramValueType) {
        if (FundUtils.isNull(value))
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
            throw new FundException(GeneralExceptionType.PARAM_VALUE_TYPE_IS_NOT_EQUAL_PARAMS_VALUE_TYPE, new Object[]{paramValueType.getTitle()});
        }
        if (!flag)
            throw new FundException(GeneralExceptionType.PARAM_VALUE_TYPE_IS_NOT_EQUAL_PARAMS_VALUE_TYPE, new Object[]{paramValueType.getTitle()});
    }

}
