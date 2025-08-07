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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentNormalImpl extends PaymentAbstract {
    public PaymentNormalImpl(JpaRepository repository, VoucherService voucherService, ParamService paramService) {
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

        if (paymentDetails.size() > 1)
            throw new FundException(PaymentExceptionType.PAYMENTDETAIL_CAN_NOT_LIST);

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
        voucherService.deleteByReferenceId(List.of(payment.getId()), VoucherType.RECEIPT_PAYMENT, payment.getPaymentDate(), userId, uuid);
        createVoucherForPayment(VoucherType.RECEIPT_PAYMENT,
                payment.getFundBranch(),
                payment.getFund(),
                payment.getPaymentDate(),
                createVoucherDetail(PaymentType.getItemById(payment.getPaymentType().getId()), paymentDetails),
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
        delete(paymentDetails.getFirst().getPayment().getId(), userId, uuid);
    }

    @Override
    public Boolean accept(PaymentType paymentType) {
        return paymentType != PaymentType.GROUP_CHECK &&
                paymentType != PaymentType.GROUP_WITHDRAWAL_ONLINE &&
                paymentType != PaymentType.GROUP_TRANSFER_ONLINE;
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

    private List<VoucherDetail> createVoucherDetail(PaymentType paymentType, List<PaymentDetail> paymentDetails) {
        validatePaymentDetails(paymentType, paymentDetails);

        return paymentDetails.stream()
                .flatMap(pd -> createVoucherDetailsForPayment(paymentType, pd).stream())
                .collect(Collectors.toList());
    }

    private void validatePaymentDetails(PaymentType paymentType, List<PaymentDetail> paymentDetails) {
        switch (paymentType) {
            case CASH:
                if (paymentDetails.stream().anyMatch(a -> FundUtils.isNull(a.getBankAccount()))) {
                    throw new FundException(PaymentExceptionType.PAYMNET_BANK_ACCOUNT_IS_NULL);
                }
                break;
            case TRANSFER:
                if (paymentDetails.stream().anyMatch(a -> FundUtils.isNull(a.getPayment().getFromSubsidiaryLedger()))) {
                    throw new FundException(PaymentExceptionType.PAYMENTDETAIL_FROM_SL_IS_NULL);
                }
                if (paymentDetails.stream().anyMatch(a -> FundUtils.isNull(a.getPayment().getToSubsidiaryLedger()))) {
                    throw new FundException(PaymentExceptionType.PAYMENTDETAIL_TO_SL_IS_NULL);
                }
                break;
        }
    }

    private List<VoucherDetail> createVoucherDetailsForPayment(PaymentType paymentType, PaymentDetail pd) {
        List<VoucherDetail> details = new ArrayList<>();
        Payment payment = pd.getPayment();
        PaymentType paymentItem = PaymentType.getItemById(payment.getPaymentType().getId());
        String comments = createVoucherDetailComments(paymentItem, pd);

        switch (paymentType) {
            case CASH:
                details.add(voucherService.createVoucherDetail(
                        payment.getFromSubsidiaryLedger(),
                        payment.getFromDetailLedger(),
                        0L,
                        pd.getAmount(),
                        comments,
                        payment.getId()
                ));
                details.add(voucherService.createVoucherDetail(
                        paramService.getSubsidiaryLedger(payment.getFund(), Consts.PARAMS_BANK_SL_DEPOSIT_NSTR),
                        pd.getDetailLedger(),
                        pd.getAmount(),
                        0L,
                        comments,
                        payment.getId()
                ));
                break;
            case CHECK:
                details.add(voucherService.createVoucherDetail(
                        paramService.getSubsidiaryLedger(payment.getFund(), Consts.PARAMS_BANK_SL_NSTR),
                        payment.getFromDetailLedger(),
                        0L,
                        pd.getAmount(),
                        comments,
                        payment.getId()
                ));
                details.add(voucherService.createVoucherDetail(
                        payment.getToSubsidiaryLedger(),
                        pd.getDetailLedger(),
                        pd.getAmount(),
                        0L,
                        comments,
                        payment.getId()
                ));
                break;
            case TRANSFER:
                details.add(voucherService.createVoucherDetail(
                        payment.getFromSubsidiaryLedger(),
                        payment.getFromDetailLedger(),
                        0L,
                        pd.getAmount(),
                        comments,
                        payment.getId()
                ));
                details.add(voucherService.createVoucherDetail(
                        payment.getToSubsidiaryLedger(),
                        pd.getDetailLedger(),
                        pd.getAmount(),
                        0L,
                        comments,
                        payment.getId()
                ));
                break;
        }
        return details;
    }

    private String createVoucherDetailComments(PaymentType paymentType, PaymentDetail paymentDetail) {
        String comments = null;
        switch (paymentType) {
            case CASH:
                comments = " دريافت فيش نقدي از " +
                        paymentDetail.getPayment().getFromDetailLedger().getName() +
                        " ش " +
                        paymentDetail.getPayment().getCode() +
                        " " +
                        paymentDetail.getBankAccount().getBank().getName();
                break;
            case CHECK:
                comments = " پرداخت چک عادی " +
                        paymentDetail.getPayment().getCode() +
                        " " +
                        paymentDetail.getPayment().getBankAccount().getBank().getName() +
                        " به تفصیل " +
                        paymentDetail.getDetailLedger().getName();
                break;
            case TRANSFER:
                comments = " پرداخت انتقالی از " +
                        paymentDetail.getPayment().getFromDetailLedger().getName() +
                        " به " +
                        paymentDetail.getDetailLedger().getName() +
                        " " +
                        paymentDetail.getPayment().getPaymentReason().getName();
                break;
        }
        return comments;
    }
}
