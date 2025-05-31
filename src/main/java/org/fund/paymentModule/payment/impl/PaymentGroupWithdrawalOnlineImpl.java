package org.fund.paymentModule.payment.impl;


import org.fund.common.FundUtils;
import org.fund.exception.FundException;
import org.fund.exception.PaymentExceptionType;
import org.fund.model.*;
import org.fund.paymentModule.payment.PaymentAbstract;
import org.fund.paymentModule.payment.constant.PaymentType;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentGroupWithdrawalOnlineImpl extends PaymentAbstract {
    public PaymentGroupWithdrawalOnlineImpl(JpaRepository repository) {
        super(repository);
    }

    @Override
    protected void validatePaymentDataForInsert(Payment payment) {
        preparationPaymentData(payment);
        payment.setPaymentStatus(repository.findOne(PaymentStatus.class, org.fund.paymentModule.payment.constant.PaymentStatus.PAYMENT_DRAFT.getId()));
        payment.setUuid(FundUtils.generateUUID().toString());
    }

    @Override
    protected void validatePaymentDetailDataForInsert(List<PaymentDetail> paymentDetails) {
        if (FundUtils.isNull(paymentDetails) || paymentDetails.size() == 0)
            throw new FundException(PaymentExceptionType.PAYMENTDETAIL_IS_NULL);

        checkDifferentPayment(paymentDetails);

        if (!paymentDetails.getFirst().getPayment().getPaymentOrigin().getId().equals(org.fund.paymentModule.payment.constant.PaymentOrigin.GROUP_WITHDRAWAL_ONLINE.getId()))
            throw new FundException(PaymentExceptionType.PAYMNET_ORIGIN_IS_NOT_GROUP_WITHDRAWAL_ONLINE);

        if (paymentDetails.getFirst().getPayment().getPaymentOrigin().getId().equals(org.fund.paymentModule.payment.constant.PaymentOrigin.REQUEST_MANAGEMENT.getId())) {
            if (paymentDetails.stream().anyMatch(a -> FundUtils.isNull(a.getId()))) {
                throw new FundException(PaymentExceptionType.PAYMNET_ORDER_ID_IS_NULL);
            }
        }
        preparationPaymentDetailData(paymentDetails);
    }

    private void preparationPaymentDetailData(List<PaymentDetail> paymentDetails) {
        PaymentStatus paymentStatus = repository.findOne(PaymentStatus.class, org.fund.paymentModule.payment.constant.PaymentStatus.PAYMENT_DRAFT.getId());
        paymentDetails.forEach(p -> {
            p.setPaymentStatus(paymentStatus);
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
        validatePaymentMasterData(payment, userId);
        validatePaymentDataForInsert(payment);
    }

    private void validatePaymentMasterData(Payment payment, Long userId) throws Exception {
        Payment oldPayment = repository.findOne(Payment.class, payment.getId());
        if (!oldPayment.getPaymentStatus().getId().equals(org.fund.paymentModule.payment.constant.PaymentStatus.PAYMENT_DRAFT.getId()))
            throw new FundException(PaymentExceptionType.PAYMNET_STATUS_IS_WRONG, new Object[]{org.fund.paymentModule.payment.constant.PaymentStatus.PAYMENT_DRAFT.getTitle()});

        if (!oldPayment.getInsertedUserId().equals(userId)) {
            Users user = repository.findOne(Users.class, oldPayment.getInsertedUserId());
            throw new FundException(PaymentExceptionType.PAYMNET_STATUS_IS_NOT_YOURS, new Object[]{user.getPerson().getFirstName() + "  " + user.getPerson().getLastName()});
        }
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
        return paymentType == PaymentType.GROUP_WITHDRAWAL_ONLINE;
    }

    @Override
    protected void validatePaymentDataForDelete(Payment payment, Long userId) throws Exception {
        validatePaymentMasterData(payment, userId);
    }

    @Override
    public void changeStatus(Payment oldPayment, org.fund.paymentModule.payment.constant.PaymentStatus toPaymentStatus, Long userId, String uuid) throws Exception {
        if (toPaymentStatus != org.fund.paymentModule.payment.constant.PaymentStatus.READY_TO_SEND_TO_CARTABE &&
                !oldPayment.getPaymentStatus().getId().equals(org.fund.paymentModule.payment.constant.PaymentStatus.PAYMENT_DRAFT.getId()))
            throw new FundException(PaymentExceptionType.PAYMNET_STATUS_IS_WRONG, new Object[]{org.fund.paymentModule.payment.constant.PaymentStatus.PAYMENT_DRAFT.getTitle()});

        if (toPaymentStatus != org.fund.paymentModule.payment.constant.PaymentStatus.PAYMENT_DRAFT &&
                !oldPayment.getPaymentStatus().getId().equals(org.fund.paymentModule.payment.constant.PaymentStatus.READY_TO_SEND_TO_CARTABE.getId()))
            throw new FundException(PaymentExceptionType.PAYMNET_STATUS_IS_WRONG, new Object[]{org.fund.paymentModule.payment.constant.PaymentStatus.READY_TO_SEND_TO_CARTABE.getTitle()});

        oldPayment.setPaymentStatus(repository.findOne(PaymentStatus.class, toPaymentStatus.getId()));
        repository.update(oldPayment, userId, uuid);
    }
}
