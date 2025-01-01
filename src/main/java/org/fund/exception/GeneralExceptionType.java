package org.fund.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GeneralExceptionType {
    SCHEMAID_ID_IS_NULL(HttpStatus.BAD_REQUEST, "general_exception.schemaId_id_is_null"),
    SCHEMAID_ID_IS_NOT_VALID(HttpStatus.BAD_REQUEST, "general_exception.tenant_not_valid"),
    ID_IS_NULL(HttpStatus.UNPROCESSABLE_ENTITY, "general_exception.id_is_null"),
    ENTITY_CANNOT_BE_NULL(HttpStatus.UNPROCESSABLE_ENTITY, "general_exception.entity_cannot_be_null")
    ;
    private final HttpStatus httpStatus;
    private final String messageKey;

    GeneralExceptionType(HttpStatus httpStatus, String messageKey) {
        this.httpStatus = httpStatus;
        this.messageKey = messageKey;
    }
}
