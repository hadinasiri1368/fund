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
import org.fund.paymentModule.payment.constant.PaymentStatus;
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
public class PaymentGroupCheckImpl extends PaymentAbstract {
    public PaymentGroupCheckImpl(JpaRepository repository, VoucherService voucherService, ParamService paramService) {
        super(repository, voucherService, paramService);
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
            p.setUuid(null);
        });
    }

    @Override
    protected void createOrReplaceVoucher(Payment payment, List<PaymentDetail> paymentDetails, boolean afterInsert, Long userId, String uuid) throws Exception {
        List<Long> paymentDetailIds = paymentDetails.stream().map(PaymentDetail::getId).collect(Collectors.toList());
        voucherService.deleteByReferenceId(paymentDetailIds, VoucherType.RECEIPT_PAYMENT, payment.getPaymentDate(), userId, uuid);
        voucherService.deleteByReferenceId(List.of(payment.getId()), VoucherType.RECEIPT_PAYMENT, payment.getPaymentDate(), userId, uuid);
        createVoucherForPayment(VoucherType.RECEIPT_PAYMENT,
                payment.getFundBranch(),
                payment.getFund(),
                payment.getPaymentDate(),
                createVoucherDetail(payment, paymentDetails),
                userId,
                uuid);
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

    private List<VoucherDetail> createVoucherDetail(Payment payment, List<PaymentDetail> paymentDetails) {
        validatePaymentDetails(paymentDetails);
        List<VoucherDetail> list = new ArrayList<>();
        for (PaymentDetail detail : paymentDetails) {
            list.add(createVoucherDetailsForPayment(null, detail));
        }
        list.add(createVoucherDetailsForPayment(payment, paymentDetails));
        return list;
    }

    private void validatePaymentDetails(List<PaymentDetail> paymentDetails) {
        if (paymentDetails.stream().anyMatch(a -> FundUtils.isNull(a.getDetailLedger()))) {
            throw new FundException(PaymentExceptionType.PAYMENTDETAIL_TO_DL_IS_NULL);
        }
    }

    private VoucherDetail createVoucherDetailsForPayment(Payment payment, List<PaymentDetail> paymentDetails) {
        String comments = createVoucherDetailComments(payment, null);
        Long amount = paymentDetails.stream().mapToLong(d -> d.getAmount()).sum();
        return voucherService.createVoucherDetail(
                paramService.getSubsidiaryLedger(payment.getFund(), Consts.PARAMS_BANK_SL_DEPOSIT_NSTR),
                payment.getFromDetailLedger(),
                0L,
                amount,
                comments,
                payment.getId()
        );
    }

    private VoucherDetail createVoucherDetailsForPayment(Payment payment, PaymentDetail pd) {
        String comments = createVoucherDetailComments(null, pd);

        return voucherService.createVoucherDetail(
                payment.getFromSubsidiaryLedger(),
                pd.getDetailLedger(),
                pd.getAmount(),
                0L,
                comments,
                pd.getId()
        );
    }

    private String createVoucherDetailComments(Payment payment, PaymentDetail paymentDetail) {
        if (!FundUtils.isNull(payment))
            return "پرداخت چک عادی " + payment.getCode() + " " + payment.getBankAccount().getBank().getName();
        return "پرداخت چک عادی " + payment.getCode() + " " + payment.getBankAccount().getBank().getName() + " " + paymentDetail.getDetailLedger().getName();
    }

}
