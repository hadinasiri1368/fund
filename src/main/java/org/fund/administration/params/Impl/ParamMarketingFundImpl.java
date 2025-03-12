package org.fund.administration.params.Impl;

import org.fund.common.FundUtils;
import org.fund.exception.FundException;
import org.fund.exception.ParamExceptionType;
import org.fund.model.Fund;
import org.fund.model.Params;
import org.fund.administration.params.ParamAbstract;
import org.fund.administration.params.constant.ParamType;
import org.fund.administration.params.constant.ParamValueType;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ParamMarketingFundImpl extends ParamAbstract {
    public ParamMarketingFundImpl(JpaRepository repository) {
        super(repository);
    }

    @Override
    public boolean accept() {
        return repository.findAll(Fund.class).size() > 0;
    }

    @Override
    public void validateBeforInsert(Params param) {
        validateType(param.getValue()
                , ParamValueType.getItemById(param.getParamsValueType().getId().intValue())
                , ParamType.getItemById(param.getParamsType().getId()));
        Params item = getParams(param.getCode()
                , ParamValueType.getItemById(param.getParamsValueType().getId().intValue()));
        if (param.getIsGlobal()) {
            if (!FundUtils.isNull(item) || !FundUtils.isNull(param.getFund()))
                throw new FundException(ParamExceptionType.PARAM_CODE_EXISTS, new Object[]{param.getCode()});
        } else {
            if (!FundUtils.isNull(item) && param.getFund().equals(item.getFund()))
                throw new FundException(ParamExceptionType.PARAM_CODE_EXISTS, new Object[]{param.getCode()});
        }
    }

    @Override
    public void validateBeforUpdate(Params param, Params oldParam) {
        if (!oldParam.getValue().equals(param.getValue())) {
            validateType(param.getValue()
                    , ParamValueType.getItemById(param.getParamsValueType().getId().intValue())
                    , ParamType.getItemById(param.getParamsType().getId()));
        }
        if (param.getIsGlobal() && !FundUtils.isNull(param.getFund()))
            throw new FundException(ParamExceptionType.PARAM_HAVE_TO_GLOBAL, new Object[]{param.getCode()});

        Params item = getParams(param.getCode()
                , ParamValueType.getItemById(param.getParamsValueType().getId().intValue()));

        if (!oldParam.getIsGlobal() && param.getIsGlobal() && !FundUtils.isNull(item))
            throw new FundException(ParamExceptionType.PARAM_CODE_EXISTS, new Object[]{param.getCode()});

        if (!oldParam.getFund().equals(param.getFund()) && !FundUtils.isNull(item) && item.getFund().equals(param.getFund()))
            throw new FundException(ParamExceptionType.PARAM_CODE_EXISTS, new Object[]{param.getCode()});

        if (!oldParam.getCode().equals(param.getCode()) && !FundUtils.isNull(item) && item.getFund().equals(param.getFund()))
            throw new FundException(ParamExceptionType.PARAM_CODE_EXISTS, new Object[]{param.getCode()});
    }
}
