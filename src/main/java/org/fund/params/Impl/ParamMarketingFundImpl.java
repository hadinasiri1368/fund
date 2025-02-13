package org.fund.params.Impl;

import org.apache.tomcat.util.bcel.classfile.Constant;
import org.fund.common.FundUtils;
import org.fund.constant.Consts;
import org.fund.exception.AuthenticationExceptionType;
import org.fund.exception.FundException;
import org.fund.exception.GeneralExceptionType;
import org.fund.model.Fund;
import org.fund.model.Params;
import org.fund.params.ParamAbstract;
import org.fund.params.ParamValueType;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;

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
    public void setIsGlobal(Params param) {
        if (param.getIsGlobal()) {
            repository.findAll(Params.class).stream()
                    .filter(a -> a.getCode().equals(param.getCode()))
                    .findAny()
                    .ifPresent(name -> {
                        throw new FundException(GeneralExceptionType.PARAM_NOT_FOUND, new Object[]{param.getCode()});
                    });
        }
    }
}
