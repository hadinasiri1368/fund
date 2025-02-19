package org.fund.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FundExceptionType {
    CAN_NOT_INSERT_FUND(HttpStatus.INTERNAL_SERVER_ERROR, "fund_fix.can_not_insert_fund"),
    MMTP_CONFIG_NOT_EXISTS(HttpStatus.INTERNAL_SERVER_ERROR, "fund_etf.mmtp_config_not_exists");
    private final HttpStatus httpStatus;
    private final String messageKey;

    FundExceptionType(HttpStatus httpStatus, String messageKey) {
        this.httpStatus = httpStatus;
        this.messageKey = messageKey;
    }
}
