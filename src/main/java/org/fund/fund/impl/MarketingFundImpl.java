package org.fund.fund.impl;

import org.fund.fund.FundAbstract;
import org.fund.model.Fund;
import org.fund.model.MmtpConfig;
import org.fund.repository.JpaRepository;
import org.fund.service.CommonFundService;

public class MarketingFundImpl extends FundAbstract {
    public MarketingFundImpl(JpaRepository repository, CommonFundService commonFundService) {
        super(repository, commonFundService);
    }

    @Override
    public MmtpConfig getMmtpConfig(Fund fund) {
        return null;
    }

    @Override
    public boolean accept() {
        return commonFundService.isMarketingFund();
    }

    @Override
    public void validateBeforInsert(Fund fund) {

    }

    @Override
    public void deleteMmtpConfig(Fund fund, long userId, String uuid) throws Exception {

    }

    @Override
    public void insertMmtpConfig(Fund fund, long userId, String uuid) throws Exception {

    }
}
