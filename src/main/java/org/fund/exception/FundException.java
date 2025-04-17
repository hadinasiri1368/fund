package org.fund.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.fund.model.Customer;
import org.springframework.http.HttpStatus;

import java.util.List;

@Slf4j
@Getter
public class FundException extends RuntimeException {
    private final HttpStatus status;
    private final Object[] params;

    public FundException(GeneralExceptionType generalExceptionType) {
        super(generalExceptionType.getMessageKey());
        this.status = generalExceptionType.getHttpStatus();
        this.params = null;
    }

    public FundException(GeneralExceptionType generalExceptionType, Object[] params) {
        super(generalExceptionType.getMessageKey());
        this.status = generalExceptionType.getHttpStatus();
        this.params = params;
    }

    public FundException(AuthenticationExceptionType authenticationExceptionType) {
        super(authenticationExceptionType.getMessageKey());
        this.status = authenticationExceptionType.getHttpStatus();
        this.params = null;
    }

    public FundException(AuthenticationExceptionType authenticationExceptionType, Object[] params) {
        super(authenticationExceptionType.getMessageKey());
        this.status = authenticationExceptionType.getHttpStatus();
        this.params = params;
    }

    public FundException(ParamExceptionType paramExceptionType) {
        super(paramExceptionType.getMessageKey());
        this.status = paramExceptionType.getHttpStatus();
        this.params = null;
    }

    public FundException(ParamExceptionType paramExceptionType, Object[] params) {
        super(paramExceptionType.getMessageKey());
        this.status = paramExceptionType.getHttpStatus();
        this.params = params;
    }

    public FundException(FundExceptionType fundExceptionType) {
        super(fundExceptionType.getMessageKey());
        this.status = fundExceptionType.getHttpStatus();
        this.params = null;
    }

    public FundException(FundExceptionType fundExceptionType, Object[] params) {
        super(fundExceptionType.getMessageKey());
        this.status = fundExceptionType.getHttpStatus();
        this.params = params;
    }

    public FundException(CustomerExceptionType fundExceptionType) {
        super(fundExceptionType.getMessageKey());
        this.status = fundExceptionType.getHttpStatus();
        this.params = null;
    }

    public FundException(CustomerExceptionType fundExceptionType, Object[] params) {
        super(fundExceptionType.getMessageKey());
        this.status = fundExceptionType.getHttpStatus();
        this.params = params;
    }
}
