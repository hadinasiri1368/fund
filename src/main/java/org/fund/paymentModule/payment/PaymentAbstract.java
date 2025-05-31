package org.fund.paymentModule.payment;

import org.fund.common.FundUtils;
import org.fund.exception.FundException;
import org.fund.exception.PaymentExceptionType;
import org.fund.model.Fund;
import org.fund.model.Payment;
import org.fund.model.PaymentDetail;
import org.fund.paymentModule.payment.constant.PaymentStatus;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public abstract class PaymentAbstract implements IPayment {
    protected final JpaRepository repository;

    public PaymentAbstract(JpaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public void insert(Payment payment, List<PaymentDetail> paymentDetails, Long userId, String uuid) throws Exception {
        validatePaymentDate(payment.getFund(), payment.getPaymentDate());
        validatePaymentDataForInsert(payment);
        validatePaymentDetailDataForInsert(paymentDetails);
        repository.save(payment, userId, uuid);
        repository.batchInsert(paymentDetails, userId, uuid);
        createOrReplaceVoucher(payment, paymentDetails, true, userId, uuid);
    }

    @Transactional
    @Override
    public void update(Payment payment, List<PaymentDetail> paymentDetails, Long userId, String uuid) throws Exception {
        validatePaymentDate(payment.getFund(), payment.getPaymentDate());
        validatePaymentIsManual(payment.getIsManual());
        validatePaymentDataForUpdate(payment, userId);
        validatePaymentDetailDataForUpdate(paymentDetails);
        repository.update(payment, userId, uuid);
        delete(payment.getId(), userId, uuid);
        repository.batchInsert(paymentDetails, userId, uuid);
        createOrReplaceVoucher(payment, paymentDetails, false, userId, uuid);
    }

    @Override
    public void delete(Long paymentId, Long userId, String uuid) throws Exception {
        Payment payment = repository.findOne(Payment.class, paymentId);
        validatePaymentDate(payment.getFund(), payment.getPaymentDate());
        validatePaymentIsManual(payment.getIsManual());
        validatePaymentDataForDelete(payment, userId);
        createOrReplaceVoucher(payment, null, false, userId, uuid);
        List<PaymentDetail> paymentDetails = repository.findAll(PaymentDetail.class).stream().filter(a -> a.getPayment().getId().equals(paymentId)).toList();
        delete(paymentDetails, userId, uuid);
        repository.removeById(Payment.class, paymentId, userId, uuid);
    }

    protected abstract void validatePaymentDataForInsert(Payment payment) throws Exception;

    protected abstract void validatePaymentDetailDataForInsert(List<PaymentDetail> paymentDetails) throws Exception;

    protected abstract void validatePaymentDataForUpdate(Payment payment, Long userId) throws Exception;

    protected abstract void validatePaymentDataForDelete(Payment payment, Long userId) throws Exception;

    protected abstract void validatePaymentDetailDataForUpdate(List<PaymentDetail> paymentDetails) throws Exception;

    protected abstract void createOrReplaceVoucher(Payment payment, List<PaymentDetail> paymentDetails, boolean afterInsert, Long userId, String uuid) throws Exception;

    public abstract void delete(List<PaymentDetail> paymentDetails, Long userId, String uuid) throws Exception;

    public abstract void changeStatus(Payment oldPayment, PaymentStatus toPaymentStatus, Long userId, String uuid) throws Exception;

    private void validatePaymentDate(Fund fund, String paymentDate) throws Exception {
        String lastNavDate = getLastNavDate(fund);
        if (lastNavDate.compareTo(paymentDate) <= 0)
            throw new FundException(PaymentExceptionType.PAYMENTDATE_IS_BEFOR_LAST_NAV_DATE, new Object[]{paymentDate});

    }

    private void validatePaymentIsManual(boolean isManual) throws Exception {
        if (!isManual)
            throw new FundException(PaymentExceptionType.PAYMENT_IS_AUTOMATIC);
    }

    private String getLastNavDate(Fund fund) {
//        throw new RuntimeException("nav has not been launched yet");
        return "9999/99/99";
    }

    protected void preparationPaymentData(Payment payment) {
        payment.setPaymentStatus(null);
        payment.setPaymentOrigin(null);
        payment.setUuid(null);
        payment.setSentToBank(false);
        if (!FundUtils.isNull(payment.getPaymentReason().getFromSubsidiaryLedger()))
            payment.setFromSubsidiaryLedger(payment.getPaymentReason().getFromSubsidiaryLedger());
        if (!FundUtils.isNull(payment.getPaymentReason().getToSubsidiaryLedger()))
            payment.setToSubsidiaryLedger(payment.getPaymentReason().getToSubsidiaryLedger());
    }

    protected void checkDifferentPayment(List<PaymentDetail> paymentDetails) {
        boolean hasDuplicate = paymentDetails.stream()
                .collect(Collectors.groupingBy(pd -> pd.getPayment().getId(), Collectors.counting()))
                .values()
                .stream()
                .anyMatch(count -> count > 1);
        if (hasDuplicate)
            throw new FundException(PaymentExceptionType.PAYMENTDETAIL_HAS_DIFFERENT_PAYMENT);
    }
}
