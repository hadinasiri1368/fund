package org.fund.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PaymentExceptionType {
    PAYMENTDETAIL_IS_NULL(HttpStatus.INTERNAL_SERVER_ERROR, "payment.paymentDetail_is_null"),
    PAYMENTDATE_IS_BEFOR_LAST_NAV_DATE(HttpStatus.INTERNAL_SERVER_ERROR, "payment.paymentdate_is_befor_last_nav_date"),
    PAYMENT_IS_AUTOMATIC(HttpStatus.INTERNAL_SERVER_ERROR, "payment.payment_is_automatic"),
    PAYMENTDETAIL_HAS_DIFFERENT_PAYMENT(HttpStatus.INTERNAL_SERVER_ERROR, "payment.paymentDetail_has_different_payment"),
    PAYMNET_STATUS_IS_WRONG(HttpStatus.INTERNAL_SERVER_ERROR, "payment.paymnet_status_is_wrong"),
    PAYMNET_STATUS_IS_NOT_YOURS(HttpStatus.INTERNAL_SERVER_ERROR, "payment.paymnet_status_is_not_yours"),
    PAYMNET_ORIGIN_IS_NOT_GROUP_WITHDRAWAL_ONLINE(HttpStatus.INTERNAL_SERVER_ERROR, "payment.paymnet_origin_is_not_group_withdrawal_online"),
    PAYMNET_ORDER_ID_IS_NULL(HttpStatus.INTERNAL_SERVER_ERROR, "payment.paymnet_order_id_is_null"),
    PAYMENTDETAIL_CAN_NOT_LIST(HttpStatus.INTERNAL_SERVER_ERROR, "payment.paymentDetail_can_not_list");
    private final HttpStatus httpStatus;
    private final String messageKey;

    PaymentExceptionType(HttpStatus httpStatus, String messageKey) {
        this.httpStatus = httpStatus;
        this.messageKey = messageKey;
    }
}
