package org.fund.administration.params.impl;

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
public class ParamFixFundImpl extends ParamAbstract {
    public ParamFixFundImpl(JpaRepository repository) {
        super(repository);
    }

    @Override
    public boolean accept() {
        return repository.findAll(Fund.class).size() == 0;
    }

    @Override
    public void validateBeforInsert(Params param) {
        validateType(param.getValue()
                , ParamValueType.getItemById(param.getParamsValueType().getId().intValue())
                , ParamType.getItemById(param.getParamsType().getId()));
        Params item = getParams(param.getCode()
                , ParamValueType.getItemById(param.getParamsValueType().getId().intValue()));
        if (!FundUtils.isNull(item))
            throw new FundException(ParamExceptionType.PARAM_CODE_EXISTS, new Object[]{param.getCode()});
        if (!param.getIsGlobal() || !FundUtils.isNull(param.getFund()))
            throw new FundException(ParamExceptionType.PARAM_HAVE_TO_GLOBAL, new Object[]{param.getCode()});
    }

    @Override
    public void validateBeforUpdate(Params param, Params oldParam) {
        if (!oldParam.getValue().equals(param.getValue())) {
            validateType(param.getValue()
                    , ParamValueType.getItemById(param.getParamsValueType().getId().intValue())
                    , ParamType.getItemById(param.getParamsType().getId()));
        }
        if (!param.getIsGlobal() || !FundUtils.isNull(param.getFund()))
            throw new FundException(ParamExceptionType.PARAM_HAVE_TO_GLOBAL, new Object[]{param.getCode()});

        if (!oldParam.getCode().equals(param.getCode())) {
            Params item = getParams(param.getCode()
                    , ParamValueType.getItemById(param.getParamsValueType().getId().intValue()));

            if (item.getId().equals(param.getId()))
                throw new FundException(ParamExceptionType.PARAM_CODE_EXISTS, new Object[]{param.getCode()});
        }
    }
}
