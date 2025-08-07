package org.fund.paymentModule.payment.impl;


import org.fund.accounting.voucher.VoucherService;
import org.fund.accounting.voucher.constant.VoucherType;
import org.fund.administration.params.ParamService;
import org.fund.common.DateUtils;
import org.fund.common.FundUtils;
import org.fund.constant.Consts;
import org.fund.exception.FundException;
import org.fund.exception.PaymentExceptionType;
import org.fund.model.*;
import org.fund.paymentModule.payment.PaymentAbstract;
import org.fund.paymentModule.payment.constant.PaymentType;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentGroupWithdrawalOnlineImpl extends PaymentAbstract {
    public PaymentGroupWithdrawalOnlineImpl(JpaRepository repository, VoucherService voucherService, ParamService paramService) {
        super(repository, voucherService, paramService);
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
        List<Long> paymentDetailIds = paymentDetails.stream().map(PaymentDetail::getId).collect(Collectors.toList());
        voucherService.deleteByReferenceId(paymentDetailIds, org.fund.accounting.voucher.constant.VoucherType.RECEIPT_PAYMENT, payment.getPaymentDate(), userId, uuid);
        voucherService.deleteByReferenceId(List.of(payment.getId()), org.fund.accounting.voucher.constant.VoucherType.RECEIPT_PAYMENT, payment.getPaymentDate(), userId, uuid);
        createVoucherForPayment(VoucherType.RECEIPT_PAYMENT,
                payment.getFundBranch(),
                payment.getFund(),
                payment.getPaymentDate(),
                createVoucherDetail(paymentDetails),
                userId,
                uuid);
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

    private void createVoucherForPayment(VoucherType voucherType, FundBranch fundBranch, Fund fund, String voucherDate, List<VoucherDetail> voucherDetails, Long userId, String uuid) throws Exception {
        String hql = "select v from voucher v where v.voucherType.id=:voucherTypeId and v.voucherDate=:voucherDate";
        Map<String, Object> params = new HashMap<>();
        params.put("voucherTypeId", voucherType.getId());
        params.put("voucherDate", voucherDate);
        List<Voucher> vouchers = repository.listObjectByQuery(hql, params);
        Long lineNumber;
        Voucher voucher;
        if (FundUtils.isNull(vouchers) || vouchers.isEmpty()) {
            voucher = createVoucher(voucherType, voucherDate, fundBranch, fund);
            voucherService.insert(voucher, voucherDetails, userId, uuid);
            lineNumber = 0L;
        } else {
            voucher = vouchers.getFirst();
            lineNumber = voucherDetails.stream()
                    .mapToLong(a -> a.getLineNumber())
                    .max().orElse(0L);
        }
        for (VoucherDetail detail : voucherDetails) {
            detail.setVoucher(voucher);
            detail.setLineNumber(++lineNumber);
        }
        voucherService.insert(voucherDetails, userId, uuid);
    }

    private Voucher createVoucher(VoucherType voucherType, String voucherDate, FundBranch fundBranch, Fund fund) {
        String comments = String.format("سند %s مورخ %s", voucherType.getTitle(), voucherDate);
        return Voucher.builder()
                .comments(comments)
                .voucherType(repository.findOne(org.fund.model.VoucherType.class, voucherType.getId()))
                .fundBranch(fundBranch)
                .fund(fund)
                .code(getVoucherCode())
                .voucherDate(voucherDate)
                .isManual(false)
                .build();
    }

    private String getVoucherCode() {
        String[] date = DateUtils.getTodayJalali().split("/");
        LocalTime now = LocalTime.now();
        return date[0] + date[1] + date[2] + now.format(DateTimeFormatter.ofPattern("HH")) + now.format(DateTimeFormatter.ofPattern("mm"));
    }

    private List<VoucherDetail> createVoucherDetail(List<PaymentDetail> paymentDetails) {
        validatePaymentDetails(paymentDetails);
        List<VoucherDetail> list = new ArrayList<>();
        for (PaymentDetail detail : paymentDetails) {
            list.addAll(createVoucherDetailsForPayment(detail));
        }
        return list;
    }

    private void validatePaymentDetails(List<PaymentDetail> paymentDetails) {
        if (paymentDetails.stream().anyMatch(a -> FundUtils.isNull(a.getDetailLedger()))) {
            throw new FundException(PaymentExceptionType.PAYMENTDETAIL_TO_DL_IS_NULL);
        }
    }

    private List<VoucherDetail> createVoucherDetailsForPayment(PaymentDetail paymentDetail) {
        String comments = createVoucherDetailComments(paymentDetail);
        List<VoucherDetail> list = new ArrayList<>();

        list.add(voucherService.createVoucherDetail(
                paramService.getSubsidiaryLedger(paymentDetail.getPayment().getFund(), Consts.PARAMS_BANK_SL_DEPOSIT_NSTR),
                paymentDetail.getPayment().getFromDetailLedger(),
                0L,
                paymentDetail.getAmount(),
                comments,
                paymentDetail.getId()
        ));

        list.add(voucherService.createVoucherDetail(
                paymentDetail.getPayment().getToSubsidiaryLedger(),
                paymentDetail.getDetailLedger(),
                paymentDetail.getAmount(),
                0L,
                comments,
                paymentDetail.getId()
        ));
        return list;
    }

    private String createVoucherDetailComments(PaymentDetail paymentDetail) {
        return "برداشت آنلاین توسط  " + paymentDetail.getDetailLedger().getName();
    }
}
