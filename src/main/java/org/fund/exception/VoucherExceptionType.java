package org.fund.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum VoucherExceptionType {

    VOUCHER_DETAIL_IS_NULL(HttpStatus.INTERNAL_SERVER_ERROR, "voucher.voucherDetail_is_null"),
    VOUCHER_DETAIL_HAS_DIFFERENT_VOUCHER(HttpStatus.INTERNAL_SERVER_ERROR, "voucher.voucherDetail_has_different_voucher"),
    VOUCHER_DETAIL_HAS_NULL_DEBIT_AND_CREDIT(HttpStatus.INTERNAL_SERVER_ERROR, "voucher.voucherDetail_has_null_debit_and_credit"),
    CAN_NOT_CHANGE_VOUCHER(HttpStatus.INTERNAL_SERVER_ERROR, "voucher.can_not_change_voucher"),
    VOUCHER_DETAIL_DEBIT_IS_NOT_EQUAL_CREDIT(HttpStatus.INTERNAL_SERVER_ERROR, "voucher.voucherDetail_debit_is_not_equal_credit"),
    CAN_NOT_CHANGE_STATUS(HttpStatus.INTERNAL_SERVER_ERROR, "voucher.can_not_change_status");
    private final HttpStatus httpStatus;
    private final String messageKey;

    VoucherExceptionType(HttpStatus httpStatus, String messageKey) {
        this.httpStatus = httpStatus;
        this.messageKey = messageKey;
    }
}
