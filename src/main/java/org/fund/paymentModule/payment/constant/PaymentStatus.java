package org.fund.paymentModule.payment.constant;

import java.util.Arrays;
import java.util.Objects;

public enum PaymentStatus {
    DRAFT(1L, "پيش نويس","PAYMENT_DETAIL"),
    APPROVED(2L, "تاييد شده","PAYMENT_DETAIL"),
    REJECTED(3L, "رد شده","PAYMENT_DETAIL"),
    SENT_TO_BANK(5L, "حواله","PAYMENT_DETAIL"),
    NEED_TO_CHECK(6L, "نیاز به بررسی","PAYMENT_DETAIL"),
    SENT_TO_CARTABLE(7L,  "ارسال شده به کارتابل","PAYMENT_DETAIL"),
    PAYMENT_DRAFT(8L,  "پيش نويس","PAYMENT"),
    READY_TO_SEND_TO_CARTABE(9L,  "آماده ارسال به کارتابل","PAYMENT"),
    AWAITING_SIGNATURE_APPROVAL(10L,  "در انتظار تایید صاحبین امضا","PAYMENT"),
    READY_FOR_INQUIRY(11L,  "آماده استعلام","PAYMENT"),
    REJECTED_BY_APPROVAL(12L,  "رد شده توسط صاحبین امضا","PAYMENT"),
    IN_PROGRESS(13L,  "در جریان","PAYMENT"),
    TERMINATED(14L,  "خاتمه یافته","PAYMENT"),
    ERROR(15L,  "دارای خطا","PAYMENT"),
    IN_LINE_TO_SEND_TO_BANK(16L,  "در صف ارسال به بانک","PAYMENT");


    private final Long id;
    private final String title;
    private final String usageType;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUsageType() {
        return usageType;
    }

    PaymentStatus(Long id, String title, String usageType) {
        this.id = id;
        this.title = title;
        this.usageType = usageType;
    }

    public static PaymentStatus getItemById(Long id) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst()
                .orElse(null);
    }
}
