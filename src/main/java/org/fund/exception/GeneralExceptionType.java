package org.fund.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GeneralExceptionType {
    SCHEMAID_ID_IS_NULL(HttpStatus.BAD_REQUEST, "general_exception.schemaId_id_is_null"),
    SCHEMAID_ID_IS_NOT_VALID(HttpStatus.BAD_REQUEST, "general_exception.tenant_not_valid"),
    ID_IS_NULL(HttpStatus.UNPROCESSABLE_ENTITY, "general_exception.id_is_null"),
    ENTITY_CANNOT_BE_NULL(HttpStatus.UNPROCESSABLE_ENTITY, "general_exception.entity_cannot_be_null"),
    DATE_CANNOT_BE_NULL(HttpStatus.BAD_REQUEST, "general_exception.date_cannot_be_null"),
    DATE_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "validation.PersianDateNotValid.message"),
    PARAM_TYPE_IS_NOT_EQUAL_OUTPUT_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "general_exception.param_type_is_not_equal_output_type"),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "general_exception.unknown_error"),
    DEFAULT_ACTIVE_FUND_NOT_FOUND(HttpStatus.NOT_FOUND, "general_exception.default_active_fund_not_found"),
    PARAM_NOT_FOUND(HttpStatus.NOT_FOUND, "general_exception.param_not_found"),
    PARAM_VALUE_TYPE_IS_NOT_EQUAL_PARAMS_VALUE_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "general_exception.param_value_type_is_not_equal_params_value_type"),
    PARAM_CODE_EXISTS(HttpStatus.CONFLICT, "general_exception.param_code_exists");
    private final HttpStatus httpStatus;
    private final String messageKey;

    GeneralExceptionType(HttpStatus httpStatus, String messageKey) {
        this.httpStatus = httpStatus;
        this.messageKey = messageKey;
    }
}
