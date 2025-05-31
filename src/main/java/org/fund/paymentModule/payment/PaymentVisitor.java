package org.fund.paymentModule.payment;

import org.fund.administration.fund.IFund;
import org.fund.model.Payment;
import org.fund.model.PaymentDetail;
import org.fund.paymentModule.payment.constant.PaymentStatus;
import org.fund.paymentModule.payment.constant.PaymentType;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentVisitor {
    private final List<IPayment> payments;
    private final JpaRepository repository;

    public PaymentVisitor(List<IPayment> payments, JpaRepository repository) {
        this.payments = payments;
        this.repository = repository;
    }

    public void insert(Payment payment, List<PaymentDetail> paymentDetails, Long userId, String uuid) throws Exception {
        getPayment(PaymentType.getItemById(payment.getPaymentType().getId())).insert(payment, paymentDetails, userId, uuid);
    }

    public void update(Payment payment, List<PaymentDetail> paymentDetails, Long userId, String uuid) throws Exception {
        getPayment(PaymentType.getItemById(payment.getPaymentType().getId())).update(payment, paymentDetails, userId, uuid);
    }

    public void delete(Long paymentId, Long userId, String uuid) throws Exception {
        Payment oldPayment = repository.findOne(Payment.class, paymentId);
        getPayment(PaymentType.getItemById(oldPayment.getPaymentType().getId())).delete(paymentId, userId, uuid);
    }

    public void delete(List<PaymentDetail> paymentDetails, Long userId, String uuid) throws Exception {
        getPayment(PaymentType.getItemById(paymentDetails.getFirst().getPayment().getPaymentType().getId())).delete(paymentDetails, userId, uuid);
    }

    public void changeStatus(Payment oldPayment, PaymentStatus toPaymentStatus, Long userId, String uuid) throws Exception {
        getPayment(PaymentType.getItemById(oldPayment.getPaymentType().getId())).changeStatus(oldPayment, toPaymentStatus, userId, uuid);
    }

    private IPayment getPayment(PaymentType paymentType) {
        for (IPayment p : payments) {
            if (p.accept(paymentType)) {
                return p;
            }
        }
        return null;
    }
}
