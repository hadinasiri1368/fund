package org.fund.administration.fund.impl;

import org.fund.common.FundUtils;
import org.fund.exception.FundException;
import org.fund.exception.FundExceptionType;
import org.fund.administration.fund.FundAbstract;
import org.fund.model.Fund;
import org.fund.model.MmtpConfig;
import org.fund.repository.JpaRepository;
import org.fund.service.CommonFundService;

public class EtfFundImpl extends FundAbstract {
    public EtfFundImpl(JpaRepository repository, CommonFundService commonFundService) {
        super(repository, commonFundService);
    }

    @Override
    public MmtpConfig getMmtpConfig(Fund fund) {
        return repository.findAll(MmtpConfig.class).stream()
                .filter(a -> a.getFund().equals(fund))
                .findFirst().orElseThrow(() -> new FundException(FundExceptionType.MMTP_CONFIG_NOT_EXISTS));
    }

    @Override
    public boolean accept() {
        return commonFundService.isEtfFund();
    }

    @Override
    public void validateBeforInsert(Fund fund) {
        if (repository.findAll(Fund.class).size() > 0)
            throw new FundException(FundExceptionType.CAN_NOT_INSERT_FUND);
    }

    @Override
    public void deleteMmtpConfig(Fund fund, long userId, String uuid) throws Exception {
        MmtpConfig mmtpConfig = repository.findAll(MmtpConfig.class).stream()
                .filter(a -> a.getFund().equals(fund))
                .findFirst()
                .orElse(null);
        if (!FundUtils.isNull(mmtpConfig)) {
            repository.remove(mmtpConfig, userId, uuid);
        }
    }

    @Override
    public void insertMmtpConfig(Fund fund, long userId, String uuid) throws Exception {
        MmtpConfig mmtpConfig = repository.findAll(MmtpConfig.class).stream()
                .filter(a -> a.getFund().equals(getDefaultFund()))
                .findFirst()
                .orElse(null);

        mmtpConfig.setFund(fund);
        mmtpConfig.setInsertedUserId(null);
        mmtpConfig.setUpdatedUserId(null);
        mmtpConfig.setInsertedDateTime(null);
        mmtpConfig.setUpdatedDateTime(null);
        mmtpConfig.setId(null);
        repository.save(mmtpConfig, userId, uuid);
    }

    @Override
    public Fund getDefaultFund() {
        return repository.findAll(Fund.class).stream().findFirst().get();
    }
}
