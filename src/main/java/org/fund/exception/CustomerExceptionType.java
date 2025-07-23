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
    HAS_NOT_DETAILLEDGER(HttpStatus.INTERNAL_SERVER_ERROR, "customer.has_not_detailLedger"),
    HAS_NOT_BANKACCOUNT(HttpStatus.INTERNAL_SERVER_ERROR, "customer.has_not_bankAccount"),
    EMAIL_IS_INVALID(HttpStatus.INTERNAL_SERVER_ERROR, "customer.email_is_invalid"),
    CAN_NOT_INSERT_MORE_THAN_ONE_BANK_ACCOUNT(HttpStatus.INTERNAL_SERVER_ERROR, "customer.can_not_insert_bank_account_more_than_one");
    private final HttpStatus httpStatus;
    private final String messageKey;

    CustomerExceptionType(HttpStatus httpStatus, String messageKey) {
        this.httpStatus = httpStatus;
        this.messageKey = messageKey;
    }
}
