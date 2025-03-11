package org.fund.fund.impl;

import org.fund.common.FundUtils;
import org.fund.constant.Consts;
import org.fund.fund.FundAbstract;
import org.fund.model.Fund;
import org.fund.model.MmtpConfig;
import org.fund.params.Param;
import org.fund.params.ParamService;
import org.fund.repository.JpaRepository;
import org.fund.service.CommonFundService;

public class MarketingFundImpl extends FundAbstract {
    private final ParamService paramService;

    public MarketingFundImpl(JpaRepository repository
            , CommonFundService commonFundService
            , ParamService paramService) {
        super(repository, commonFundService);
        this.paramService = paramService;
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

    @Override
    public Fund getDefaultFund() {
        return repository.findAll(Fund.class).stream()
                .sorted((fund1, fund2) -> Long.compare(fund1.getId(), fund2.getId()))
                .filter(fund -> {
                    String terminationDate = paramService.getStringValue(fund, Consts.PARAMS_FUND_TERMINATION_DATE);
                    return (FundUtils.isNull(terminationDate) || terminationDate.equals("0000/00/00"));
                })
                .findFirst().get();
    }
}
