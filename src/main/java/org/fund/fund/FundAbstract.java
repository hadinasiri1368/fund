package org.fund.fund;

import org.fund.common.FundUtils;
import org.fund.model.*;
import org.fund.repository.JpaRepository;
import org.fund.service.CommonFundService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public abstract class FundAbstract implements IFund {
    protected final JpaRepository repository;
    protected final CommonFundService commonFundService;

    public FundAbstract(JpaRepository repository, CommonFundService commonFundService) {
        this.repository = repository;
        this.commonFundService = commonFundService;
    }

    public abstract Fund getDefaultFund();

    public abstract void insertMmtpConfig(Fund fund, long userId, String uuid) throws Exception;

    public abstract void deleteMmtpConfig(Fund fund, long userId, String uuid) throws Exception;

    public abstract void validateBeforInsert(Fund fund) throws Exception;

    @Override
    public void insert(Fund fund, Long userId, String uuid) throws Exception {
        validateBeforInsert(fund);
        repository.save(fund, userId, uuid);
        insertParam(fund, userId, uuid);
        insertWage(fund, userId, uuid);
        insertMmtpConfig(fund, userId, uuid);
    }

    @Override
    public void delete(Long fundId, Long userId, String uuid) throws Exception {
        Fund fund = getFund(fundId);
        deleteParam(fund, userId, uuid);
        deleteWage(fund, userId, uuid);
        deleteMmtpConfig(fund, userId, uuid);
        repository.removeById(Fund.class, fundId, userId, uuid);
    }

    @Override
    public void update(Fund fund, Long userId, String uuid) throws Exception {
        repository.update(fund, userId, uuid);
    }

    protected void insertParam(Fund fund, Long userId, String uuid) throws Exception {
        List<Params> list = repository.findAll(Params.class).stream()
                .filter(a -> a.getFund().equals(getDefaultFund()))
                .toList();
        if (!FundUtils.isNull(list) && !list.isEmpty()) {
            list = list.stream()
                    .map(param -> {
                        param.setFund(fund);
                        param.setInsertedUserId(null);
                        param.setUpdatedUserId(null);
                        param.setInsertedDateTime(null);
                        param.setUpdatedDateTime(null);
                        param.setId(null);
                        return param;
                    })
                    .collect(Collectors.toList());
        }
        repository.batchInsert(list, userId, uuid);
    }

    protected void insertWage(Fund fund, Long userId, String uuid) throws Exception {
        List<WageRate> list = repository.findAll(WageRate.class).stream()
                .filter(a -> a.getFund().equals(getDefaultFund()))
                .toList();
        for (WageRate wageRate : list) {
            wageRate.setFund(fund);
            wageRate.setInsertedUserId(null);
            wageRate.setUpdatedUserId(null);
            wageRate.setInsertedDateTime(null);
            wageRate.setUpdatedDateTime(null);
            wageRate.setId(null);
            repository.save(wageRate, userId, uuid);
            List<WageRateDetail> listDtail = repository.findAll(WageRateDetail.class).stream()
                    .filter(a -> a.getWageRate().getId().equals(wageRate.getId()))
                    .toList();
            listDtail = listDtail.stream()
                    .map(param -> {
                        param.setWageRate(wageRate);
                        param.setInsertedUserId(null);
                        param.setUpdatedUserId(null);
                        param.setInsertedDateTime(null);
                        param.setUpdatedDateTime(null);
                        param.setId(null);
                        return param;
                    })
                    .collect(Collectors.toList());
            repository.batchInsert(listDtail, userId, uuid);
        }
        repository.batchInsert(list, userId, uuid);
    }


    protected void deleteParam(Fund fund, Long userId, String uuid) throws Exception {
        List<Params> list = repository.findAll(Params.class).stream()
                .filter(a -> a.getFund().equals(fund))
                .toList();
        if (!FundUtils.isNull(list) && !list.isEmpty()) {
            repository.batchRemove(list, userId, uuid);
        }
    }

    protected void deleteWage(Fund fund, Long userId, String uuid) throws Exception {
        List<WageRate> list = repository.findAll(WageRate.class).stream()
                .filter(a -> a.getFund().equals(fund))
                .toList();
        for (WageRate wageRate : list) {
            List<WageRateDetail> wageRateDetails = repository.findAll(WageRateDetail.class).stream()
                    .filter(a -> a.getWageRate().getId().equals(wageRate.getId()))
                    .toList();
            repository.batchRemove(wageRateDetails, userId, uuid);
        }
        repository.batchRemove(list, userId, uuid);
    }

    private Fund getFund(Long fundId) {
        return repository.findOne(Fund.class, fundId);
    }

    @Override
    public Long getBourseAccount() {
        return repository.findAll(Company.class).stream()
                .findFirst()
                .orElse(null)
                .getBourseAccountNumber();
    }
}
