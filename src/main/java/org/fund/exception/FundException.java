package org.fund.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.List;

@Slf4j
@Getter
public class FundException extends RuntimeException {
    private final HttpStatus status;
    private final Object[]  params;

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
}
