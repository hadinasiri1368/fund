package org.fund.administration.fund;

import org.fund.model.Fund;
import org.fund.model.MmtpConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FundVisitor {
    private final List<IFund> funds;

    public FundVisitor(List<IFund> funds) {
        this.funds = funds;
    }

    public void insert(Fund fund, Long userId, String uuid) throws Exception {
        getFund().insert(fund, userId, uuid);
    }

    public void delete(Long fundId, Long userId, String uuid) throws Exception {
        getFund().delete(fundId, userId, uuid);
    }

    public void update(Fund fund, Long userId, String uuid) throws Exception {
        getFund().update(fund, userId, uuid);
    }

    public Long getBourseAccount() {
        return getFund().getBourseAccount();
    }

    public MmtpConfig getMmtpConfig(Fund fund) {
        return getFund().getMmtpConfig(fund);
    }

    public Fund getDefaultFund() {
        return getFund().getDefaultFund();
    }

    public List<Fund> list(Fund fund) {
        return getFund().list(fund);
    }


    private IFund getFund() {
        for (IFund f : funds) {
            if (f.accept()) {
                return f;
            }
        }
        return null;
    }
}
