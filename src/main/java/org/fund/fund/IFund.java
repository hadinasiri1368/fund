package org.fund.fund;

import org.fund.model.Fund;
import org.fund.model.MmtpConfig;

import java.util.List;

public interface IFund {
    void insert(Fund fund, Long userId, String uuid) throws Exception ;
    void delete(Long fundId, Long userId, String uuid) throws Exception ;
    void update(Fund fund, Long userId, String uuid) throws Exception ;
    List<Fund> list(Fund fund);

    Long getBourseAccount();
    MmtpConfig getMmtpConfig(Fund fund);
    Fund getDefaultFund();
    boolean accept();
}
