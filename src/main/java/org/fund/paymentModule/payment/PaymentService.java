package org.fund.paymentModule.payment;

import org.fund.common.FundUtils;
import org.fund.model.Payment;
import org.fund.model.PaymentDetail;
import org.fund.paymentModule.payment.constant.PaymentStatus;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    private final PaymentVisitor visitor;
    private final JpaRepository repository;

    public PaymentService(PaymentVisitor visitor, JpaRepository repository) {
        this.visitor = visitor;
        this.repository = repository;
    }

    public void insert(Payment payment, List<PaymentDetail> paymentDetails, Long userId, String uuid) throws Exception {
        visitor.insert(payment, paymentDetails, userId, uuid);
    }

    public void update(Payment payment, List<PaymentDetail> paymentDetails, Long userId, String uuid) throws Exception {
        visitor.update(payment, paymentDetails, userId, uuid);
    }

    public void delete(Long paymentId, Long userId, String uuid) throws Exception {
        visitor.delete(paymentId, userId, uuid);
    }

    public void delete(List<PaymentDetail> paymentDetails, Long userId, String uuid) throws Exception {
        visitor.delete(paymentDetails, userId, uuid);
    }

    public void changeStatus(Payment oldPayment, PaymentStatus toPaymentStatus, Long userId, String uuid) throws Exception {
        visitor.changeStatus(oldPayment, toPaymentStatus, userId, uuid);
    }

    public List<Payment> list(Long id) {
        if (!FundUtils.isNull(id))
            return repository.findAll(Payment.class).stream().filter(a -> a.getId().equals(id)).toList();
        return repository.findAll(Payment.class);
    }

    public List<PaymentDetail> detailList(Long paymentId) {
        return repository.findAll(PaymentDetail.class).stream()
                .filter(a -> a.getPayment().getId().equals(paymentId)).toList();
    }
}
