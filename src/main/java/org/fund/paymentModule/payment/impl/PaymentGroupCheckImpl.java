package org.fund.paymentModule.payment.impl;


import org.fund.common.FundUtils;
import org.fund.exception.FundException;
import org.fund.exception.PaymentExceptionType;
import org.fund.model.Payment;
import org.fund.model.PaymentDetail;
import org.fund.paymentModule.payment.PaymentAbstract;
import org.fund.paymentModule.payment.constant.PaymentStatus;
import org.fund.paymentModule.payment.constant.PaymentType;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentGroupCheckImpl extends PaymentAbstract {
    public PaymentGroupCheckImpl(JpaRepository repository) {
        super(repository);
    }

    @Override
    protected void validatePaymentDataForInsert(Payment payment) {
        preparationPaymentData(payment);
    }

    @Override
    protected void validatePaymentDetailDataForInsert(List<PaymentDetail> paymentDetails) {
        if (FundUtils.isNull(paymentDetails) || paymentDetails.size() == 0)
            throw new FundException(PaymentExceptionType.PAYMENTDETAIL_IS_NULL);

        preparationPaymentDetailData(paymentDetails);
    }

    private void preparationPaymentDetailData(List<PaymentDetail> paymentDetails) {
        paymentDetails.forEach(p -> {
            p.setPaymentStatus(null);
            p.setOrderId(null);
        });
    }

    @Override
    protected void createOrReplaceVoucher(Payment payment, List<PaymentDetail> paymentDetails, boolean afterInsert, Long userId, String uuid) throws Exception {

    }

    @Override
    protected void validatePaymentDetailDataForUpdate(List<PaymentDetail> paymentDetails) throws Exception {
        validatePaymentDetailDataForInsert(paymentDetails);
    }

    @Override
    protected void validatePaymentDataForUpdate(Payment payment, Long userId) throws Exception {
        validatePaymentDataForInsert(payment);
    }

    @Override
    public void delete(List<PaymentDetail> paymentDetails, Long userId, String uuid) throws Exception {
        checkDifferentPayment(paymentDetails);
        long paymentDetailCount = repository.findAll(PaymentDetail.class).stream()
                .filter(a -> a.getPayment().getId().equals(paymentDetails.getFirst().getId()))
                .count();
        if (paymentDetailCount == paymentDetails.size()) {
            delete(paymentDetails.getFirst().getId(), userId, uuid);
        } else
            repository.batchRemove(paymentDetails, userId, uuid);
    }

    @Override
    public Boolean accept(PaymentType paymentType) {
        return paymentType == PaymentType.GROUP_CHECK;
    }

    @Override
    protected void validatePaymentDataForDelete(Payment payment, Long userId) throws Exception {

    }

    @Override
    public void changeStatus(Payment oldPayment, PaymentStatus toPaymentStatus, Long userId, String uuid) throws Exception {

    }
}
