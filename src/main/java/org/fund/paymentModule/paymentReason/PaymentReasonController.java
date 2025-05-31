package org.fund.paymentModule.paymentReason;

import org.fund.administration.calendar.CalendarDto;
import org.fund.common.FundUtils;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.model.Calendar;
import org.fund.model.PaymentReason;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping(Consts.DEFAULT_PREFIX_API_URL)
public class PaymentReasonController {
    private final PaymentReasonService service;
    public PaymentReasonController(PaymentReasonService service) {
        this.service = service;
    }

    @PostMapping(path = Consts.DEFAULT_VERSION_API_URL + "/paymentModule/paymentReason/add")
    public void insert(@RequestBody PaymentReasonDto paymentReasonDto) throws Exception {
        service.insert(paymentReasonDto.toPaymentReason(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PutMapping(path = Consts.DEFAULT_VERSION_API_URL + "/paymentModule/paymentReason/edit")
    public void edit(@RequestBody PaymentReasonDto paymentReasonDto) throws Exception {
        service.update(paymentReasonDto.toPaymentReason(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(path = Consts.DEFAULT_VERSION_API_URL + "/paymentModule/paymentReason/remove/{id}")
    public void remove(@PathVariable @NotEmpty(fieldName = "id")
                       @ValidateField(fieldName = "id", entityClass = PaymentReason.class) Long paymentReasonId) throws Exception {
        service.delete(paymentReasonId, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/paymentModule/paymentReason/{id}")
    public PaymentReason getPaymentReasonList(@PathVariable("id") @ValidateField(fieldName = "id", entityClass = PaymentReason.class) Long paymentReasonId) {
        List<PaymentReason> list = service.list(paymentReasonId);
        return !FundUtils.isNull(list) ? list.get(0) : null;
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/paymentModule/paymentReason")
    public List<PaymentReason> getAllPaymentReasonList() {
        return service.list(null);
    }
}
