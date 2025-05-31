package org.fund.administration.fund;

import org.fund.model.Fund;
import org.fund.model.MmtpConfig;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FundService {
    private final JpaRepository repository;
    private final FundVisitor fundVisitor;

    public FundService(FundVisitor fundVisitor, JpaRepository repository) {
        this.fundVisitor = fundVisitor;
        this.repository = repository;
    }

    public void insert(Fund fund, Long userId, String uuid) throws Exception {
        fundVisitor.insert(fund, userId, uuid);
    }

    public void delete(Long fundId, Long userId, String uuid) throws Exception {
        fundVisitor.delete(fundId, userId, uuid);
    }

    public void update(Fund fund, Long userId, String uuid) throws Exception {
        fundVisitor.update(fund, userId, uuid);
    }

    public List<Fund> list(Fund fund) {
        return fundVisitor.list(fund);
    }

    public Fund getFund(Long fundId) {
        return repository.findOne(Fund.class, fundId);
    }

    public Long getBourseAccount() {
        return fundVisitor.getBourseAccount();
    }

    public MmtpConfig getMmtpConfig(Fund fund) {
        return fundVisitor.getMmtpConfig(fund);
    }

    public Fund getDefaultFund() {
        return fundVisitor.getDefaultFund();
    }
}
