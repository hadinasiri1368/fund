package org.fund.paymentModule.payment;

import org.fund.model.Payment;
import org.fund.model.PaymentDetail;
import org.fund.paymentModule.payment.constant.PaymentStatus;
import org.fund.paymentModule.payment.constant.PaymentType;

import java.util.List;

public interface IPayment {
    void insert(Payment payment, List<PaymentDetail> paymentDetails, Long userId, String uuid) throws Exception;

    void update(Payment payment, List<PaymentDetail> paymentDetails, Long userId, String uuid) throws Exception;

    void delete(Long paymentId, Long userId, String uuid) throws Exception;

    void delete(List<PaymentDetail> paymentDetails, Long userId, String uuid) throws Exception;

    void changeStatus(Payment oldPayment, PaymentStatus toPaymentStatus, Long userId, String uuid) throws Exception;

    Boolean accept(PaymentType paymentType);
}
