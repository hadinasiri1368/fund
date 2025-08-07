package org.fund.accounting.voucher.constant;

import java.util.Arrays;
import java.util.Objects;

public enum VoucherType {
    PURCHASE(1L, "خريد"),
    SALE(2L, "فروش"),
    RECEIPT_PAYMENT_DRAFT(3L, "سند پيش نويس دريافت/پرداخت"),
    SENIOR_DEBT(5L, "بدهي تقدم"),
    SHARE_SETTLEMENT(7L, "تسويه سهم"),
    BACKWARD(13L, "بازگشت ان اي وي"),
    PARTICIPATION_SETTLEMENT(14L, "تسويه مشارکت"),
    ESTABLISHMENT(15L, "استقرار"),
    GENERAL(16L, "عمومي"),
    CLOSE_PERFORMANCE(18L, "بستن عملكرد"),
    CLOSING(19L, "اختتاميه"),
    OPENING(20L, "افتتاحيه"),
    APPLY_PROFIT(21L, "اعمال سود"),
    SUBSCRIPTION(22L, "پذيره نويسي"),
    PARTICIPATION_TRANSACTION(23L, "خريد فروش مشاركت"),
    CONFIRM_ISSUANCE_REQUEST(24L, "تاييد درخواست صدور"),
    CONFIRM_ISSUANCE_CANCEL_REQUEST(25L, "تاييد لغو درخواست صدور"),
    REJECT_ISSUANCE_REQUEST(26L, "رد درخواست صدور"),
    CONFIRM_REDEMPTION_REQUEST(27L, "تاييد درخواست ابطال"),
    DAILY_FUND_CALCULATIONS(28L, "محاسبات روزانه صندوق"),
    SHARE_TRADING(29L, "خريد و فروش سهم و تقدم صندوق"),
    APPLY_CAPITAL_INCREASE(30L, "اعمال افزايش سرمايه"),
    FUTURE_TRADE(33L, "سند معاملات آتي"),
    GUARANTEE_GOODS_REQUEST(38L, "تضمين درخواست كالا"),
    ADJUST_CALCULATIONS(50L, "تعدیل محاسبات"),
    BILATERAL_TRANSFER(51L, "انتقال دوطرفه"),
    RECEIPT_PAYMENT(52L, "دریافت و پرداخت");

    private final Long id;
    private final String title;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    VoucherType(Long id, String title) {
        this.id = id;
        this.title = title;
    }



    public static VoucherType getItemById(Long id) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst()
                .orElse(null);
    }
}
