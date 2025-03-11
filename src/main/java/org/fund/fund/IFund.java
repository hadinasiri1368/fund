package org.fund.fund;

import org.fund.model.Fund;
import org.fund.model.MmtpConfig;

public interface IFund {
    void insert(Fund fund, Long userId, String uuid) throws Exception ;
    void delete(Long fundId, Long userId, String uuid) throws Exception ;
    void update(Fund fund, Long userId, String uuid) throws Exception ;

    Long getBourseAccount();
    MmtpConfig getMmtpConfig(Fund fund);
    Fund getDefaultFund();
    boolean accept();
}
