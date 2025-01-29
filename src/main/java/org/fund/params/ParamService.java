package org.fund.params;

import org.fund.common.FundUtils;
import org.fund.exception.AuthenticationExceptionType;
import org.fund.exception.FundException;
import org.fund.exception.GeneralExceptionType;
import org.fund.model.Params;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ParamService {
    private final JpaRepository repository;

    public ParamService(JpaRepository repository) {
        this.repository = repository;
    }

    public String getStringValue(String paramCode) {
        Params param = getParams(paramCode, ParamsValueType.STRING);
        if (FundUtils.isNull(param))
            return null;
        return param.getValue();
    }

    public Long getLongValue(String paramCode) {
        Params param = getParams(paramCode, ParamsValueType.NUMBER);
        if (FundUtils.isNull(param))
            return null;
        return FundUtils.longValue(param.getValue());
    }

    public Double getDoubleValue(String paramCode) {
        Params param = getParams(paramCode, ParamsValueType.NUMBER);
        if (FundUtils.isNull(param))
            return null;
        return FundUtils.doubleValue(param.getValue());
    }

    public Float getFloatValue(String paramCode) {
        Params param = getParams(paramCode, ParamsValueType.NUMBER);
        if (FundUtils.isNull(param))
            return null;
        return FundUtils.floatValue(param.getValue());
    }

    public Boolean getBooleanValue(String paramCode) {
        Params param = getParams(paramCode, ParamsValueType.BOOLEAN);
        if (FundUtils.isNull(param))
            return null;
        return FundUtils.booleanValue(param.getValue());
    }

    private Params getParams(String paramCode, ParamsValueType paramsValueType) {
        Params param = repository.findAll(Params.class).stream()
                .filter(p -> p.getCode().equals(paramCode))
                .findFirst()
                .orElse(null);
        if (!FundUtils.isNull(param) && !param.getParamsValueType().getName().equals(paramsValueType.getTitle()))
            throw new FundException(GeneralExceptionType.PARAM_TYPE_IS_NOT_EQUAL_OUTPUT_TYPE, new Object[]{param.getParamsValueType().getName()});
        return param;
    }


}
