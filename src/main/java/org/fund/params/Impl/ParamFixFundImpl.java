package org.fund.params.Impl;

import org.fund.common.FundUtils;
import org.fund.exception.FundException;
import org.fund.exception.GeneralExceptionType;
import org.fund.model.Fund;
import org.fund.model.Params;
import org.fund.params.ParamAbstract;
import org.fund.params.ParamValueType;
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
    public void setIsGlobal(Params param) {
        param.setIsGlobal(true);
    }
}
