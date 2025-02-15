package org.fund.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ParamExceptionType {
    PARAM_TYPE_IS_NOT_EQUAL_OUTPUT_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "param.param_type_is_not_equal_output_type"),
    PARAM_NOT_FOUND(HttpStatus.NOT_FOUND, "param.param_not_found"),
    PARAM_SUBSIDIARY_LEDGER_IS_NULL(HttpStatus.INTERNAL_SERVER_ERROR, "param.param_subsidiary_ledger_is_null"),
    PARAM_DETAIL_LEDGER_IS_NULL(HttpStatus.INTERNAL_SERVER_ERROR, "param.param_detail_ledger_is_null"),
    PARAM_VALUE_TYPE_IS_NOT_EQUAL_PARAMS_VALUE_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "param.param_value_type_is_not_equal_params_value_type"),
    PARAM_HAVE_TO_GLOBAL(HttpStatus.INTERNAL_SERVER_ERROR, "param.param_have_to_global"),
    PARAM_CODE_EXISTS(HttpStatus.CONFLICT, "param.param_code_exists");
    private final HttpStatus httpStatus;
    private final String messageKey;

    ParamExceptionType(HttpStatus httpStatus, String messageKey) {
        this.httpStatus = httpStatus;
        this.messageKey = messageKey;
    }
}
