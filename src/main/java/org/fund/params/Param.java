package org.fund.params;

import org.fund.authentication.otp.constant.OtpStrategyType;
import org.fund.common.FundUtils;
import org.fund.exception.FundException;
import org.fund.exception.GeneralExceptionType;
import org.fund.model.DetailLedger;
import org.fund.model.Fund;
import org.fund.model.Params;
import org.fund.model.SubsidiaryLedger;
import org.fund.repository.JpaRepository;

public interface Param {
    boolean accept();

    void insert(Params params, Long userId, String uuid) throws Exception;

    void update(Params params, Long userId, String uuid) throws Exception;

    void delete(Long paramId, Long userId, String uuid) throws Exception;

    Params getParam(Fund fund, String code);


// ------------------------------------------------------------------

    String getStringValue(String paramCode);

    Long getLongValue(String paramCode);

    Double getDoubleValue(String paramCode);

    Float getFloatValue(String paramCode);

    Boolean getBooleanValue(String paramCode);

    // ------------------------------------------------------------------
    String getStringValue(Fund fund, String paramCode);

    Long getLongValue(Fund fund, String paramCode);

    Double getDoubleValue(Fund fund, String paramCode);

    Float getFloatValue(Fund fund, String paramCode);

    Boolean getBooleanValue(Fund fund, String paramCode);

    // ------------------------------------------------------------------

    String getStringValue(Fund fund, String paramCode, String effectiveDate);

    Long getLongValue(Fund fund, String paramCode, String effectiveDate);

    Double getDoubleValue(Fund fund, String paramCode, String effectiveDate);

    Float getFloatValue(Fund fund, String paramCode, String effectiveDate);

    Boolean getBooleanValue(Fund fund, String paramCode, String effectiveDate);

    // ------------------------------------------------------------------

    Boolean getBooleanValue(String paramCode, String effectiveDate);

    String getStringValue(String paramCode, String effectiveDate);

    Long getLongValue(String paramCode, String effectiveDate);

    Double getDoubleValue(String paramCode, String effectiveDate);

    Float getFloatValue(String paramCode, String effectiveDate);

    // ------------------------------------------------------------------

    SubsidiaryLedger getSubsidiaryLedger(String paramCode);

    SubsidiaryLedger getSubsidiaryLedger(Fund fund, String paramCode);

    // ------------------------------------------------------------------

    DetailLedger getDetailLedger(String paramCode);

    DetailLedger getDetailLedger(Fund fund, String paramCode);

}
