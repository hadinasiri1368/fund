package org.fund.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CustomerExceptionType {
    CAN_NOT_EDIT_SEJAM_CUSTOMER(HttpStatus.INTERNAL_SERVER_ERROR, "customer.can_not_edit_sejam_customer"),
    CAN_NOT_EDIT_CUSTOMER_IN_APPLIED_PROFIT(HttpStatus.INTERNAL_SERVER_ERROR, "customer.can_not_edit_customer_in_applied_profit"),
    CAN_NOT_INSERT_CUSTOMER(HttpStatus.INTERNAL_SERVER_ERROR, "customer.can_not_insert_customer"),
    CAN_NOT_INSERT_BIRTHDATE_AFTER_NOW(HttpStatus.INTERNAL_SERVER_ERROR, "customer.can_not_insert_birthDate_after_now"),
    CAN_NOT_INSERT_FOREIGN_CUSTOMER(HttpStatus.INTERNAL_SERVER_ERROR, "customer.can_not_insert_foreign_customer"),
    CELLPHONE_IS_INVALID(HttpStatus.INTERNAL_SERVER_ERROR, "customer.cellPhone_is_invalid"),
    EMAIL_IS_INVALID(HttpStatus.INTERNAL_SERVER_ERROR, "customer.email_is_invalid");
    private final HttpStatus httpStatus;
    private final String messageKey;

    CustomerExceptionType(HttpStatus httpStatus, String messageKey) {
        this.httpStatus = httpStatus;
        this.messageKey = messageKey;
    }
}
