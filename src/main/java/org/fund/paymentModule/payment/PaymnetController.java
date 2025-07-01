package org.fund.paymentModule.payment;

import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.dto.GenericDtoMapper;
import org.fund.model.Payment;
import org.fund.model.PaymentDetail;
import org.fund.paymentModule.payment.constant.PaymentStatus;
import org.fund.paymentModule.payment.dto.PaymentDetailDto;
import org.fund.paymentModule.payment.dto.PaymentDto;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
@RequestMapping(Consts.DEFAULT_PREFIX_API_URL)
public class PaymnetController {
    private final PaymentService service;
    private final GenericDtoMapper mapper;

    public PaymnetController(PaymentService service, GenericDtoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping(path = Consts.DEFAULT_VERSION_API_URL + "/paymentModule/payment/add")
    public void insert(@RequestBody PaymentDto paymentDto) throws Exception {
        service.insert(mapper.toEntity(Payment.class, paymentDto), mapper.toEntityList(PaymentDetail.class, paymentDto), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PutMapping(path = Consts.DEFAULT_VERSION_API_URL + "/paymentModule/payment/edit")
    public void edit(@RequestBody PaymentDto paymentDto) throws Exception {
        service.update(mapper.toEntity(Payment.class, paymentDto), mapper.toEntityList(PaymentDetail.class, paymentDto), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(path = Consts.DEFAULT_VERSION_API_URL + "/paymentModule/payment/remove/{id}")
    public void remove(@PathVariable @NotEmpty(fieldName = "id")
                       @ValidateField(fieldName = "id", entityClass = Payment.class) Long paymentId) throws Exception {
        service.delete(paymentId, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(path = Consts.DEFAULT_VERSION_API_URL + "/paymentModule/payment/remove")
    public void remove(@RequestBody @NotEmpty(fieldName = "paymentDetailDtos") List<PaymentDetailDto> paymentDetailDtos) throws Exception {
        List<PaymentDetail> paymentDetails = new ArrayList<>();
        for (PaymentDetailDto paymentDetailDto : paymentDetailDtos) {
            paymentDetails.add(mapper.toEntity(PaymentDetail.class, paymentDetailDto));
        }
        service.delete(paymentDetails, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/paymentModule/payment/{id}")
    public Payment getList(@PathVariable @ValidateField(fieldName = "id", entityClass = Payment.class) Long id) {
        return service.list(id).getFirst();
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/paymentModule/payment")
    public List<Payment> getList() {
        return service.list(null);
    }

    @PutMapping(path = Consts.DEFAULT_VERSION_API_URL + "/paymentModule/payment/changeStatus/{paymentId}/{paymnetStatusId}")
    public void changeStatus(@PathVariable @ValidateField(fieldName = "paymentId", entityClass = Payment.class) Long paymentId,
                             @PathVariable @ValidateField(fieldName = "paymnetStatusId", entityClass = PaymentStatus.class) Long paymentStatusId) throws Exception {
        service.changeStatus(service.list(paymentId).getFirst()
                , PaymentStatus.getItemById(paymentStatusId)
                , RequestContext.getUserId()
                , RequestContext.getUuid());
    }
}
