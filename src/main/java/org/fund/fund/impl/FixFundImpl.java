package org.fund.fund.impl;


import org.fund.exception.FundException;
import org.fund.exception.FundExceptionType;
import org.fund.exception.GeneralExceptionType;
import org.fund.fund.FundAbstract;
import org.fund.model.Fund;
import org.fund.model.MmtpConfig;
import org.fund.repository.JpaRepository;
import org.fund.service.CommonFundService;

public class FixFundImpl extends FundAbstract {
    public FixFundImpl(JpaRepository repository, CommonFundService commonFundService) {
        super(repository, commonFundService);
    }

    @Override
    public MmtpConfig getMmtpConfig(Fund fund) {
        return null;
    }

    @Override
    public boolean accept() {
        return commonFundService.isFixFund();
    }

    @Override
    public void validateBeforInsert(Fund fund) throws Exception {
        if (repository.findAll(Fund.class).size() > 0)
            throw new FundException(FundExceptionType.CAN_NOT_INSERT_FUND);
    }

    @Override
    public void deleteMmtpConfig(Fund fund, long userId, String uuid) throws Exception {

    }

    @Override
    public void insertMmtpConfig(Fund fund, long userId, String uuid) throws Exception {

    }
}
