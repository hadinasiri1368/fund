package org.fund.paymentModule.payment.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.fund.common.FundUtils;
import org.fund.model.*;
import org.fund.model.view.external.Bank;
import org.fund.paymentModule.paymentReason.PaymentReasonDto;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidPersianDate;
import org.fund.validator.ValidateField;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
public class PaymentDto {
    private final JpaRepository repository;

    public PaymentDto(JpaRepository repository) {
        this.repository = repository;
    }


    private Long id;
    private String code;
    @NotEmpty(fieldName = "paymentReasonId")
    @ValidateField(fieldName = "paymentReasonId", entityClass = PaymentReason.class)
    private Long paymentReasonId;
    @ValidateField(fieldName = "fromSubsidiaryLedgerId", entityClass = SubsidiaryLedger.class)
    private Long fromSubsidiaryLedgerId;
    @ValidateField(fieldName = "toSubsidiaryLedgerId", entityClass = SubsidiaryLedger.class)
    private Long toSubsidiaryLedgerId;
    @ValidateField(fieldName = "fromDetailLedgerId", entityClass = DetailLedger.class)
    private Long fromDetailLedgerId;
    @NotEmpty(fieldName = "fundId")
    @ValidateField(fieldName = "fundId", entityClass = Fund.class)
    private Long fundId;
    @ValidateField(fieldName = "bankId", entityClass = Bank.class)
    private Long bankId;
    @NotEmpty(fieldName = "paymentTypeId")
    @ValidateField(fieldName = "paymentTypeId", entityClass = PaymentType.class)
    private Long paymentTypeId;
    @ValidateField(fieldName = "paymentStatusId", entityClass = PaymentStatus.class)
    private Long paymentStatusId;
    @ValidateField(fieldName = "paymentOriginId", entityClass = PaymentOrigin.class)
    private Long paymentOriginId;
    @ValidateField(fieldName = "bankAccountId", entityClass = BankAccount.class)
    private Long bankAccountId;
    @NotEmpty(fieldName = "paymentDate")
    @ValidPersianDate(fieldName = "paymentDate")
    private String paymentDate;
    private String comments;
    @NotEmpty(fieldName = "isManual")
    private Boolean isManual;
    @NotEmpty(fieldName = "sentToBank")
    private Boolean sentToBank;
    private String uuid;
    @NotEmpty(fieldName = "PaymentDetails")
    private List<PaymentDetailDto> PaymentDetails;

    public Payment toPayment() {
        ObjectMapper objectMapper = new ObjectMapper();
        Payment payment = objectMapper.convertValue(this, Payment.class);
        payment.setPaymentReason(getPaymentReason(this.paymentReasonId));
        payment.setFromSubsidiaryLedger(getSubsidiaryLedger(this.fromSubsidiaryLedgerId));
        payment.setToSubsidiaryLedger(getSubsidiaryLedger(this.toSubsidiaryLedgerId));
        payment.setFromDetailLedger(getDetailLedger(this.fromDetailLedgerId));
        payment.setFund(getFund(this.fundId));
        payment.setBank(getBank(this.bankId));
        payment.setPaymentType(getPaymentType(this.paymentTypeId));
        payment.setPaymentStatus(getPaymentStatus(this.paymentStatusId));
        payment.setPaymentOrigin(getPaymentOrigin(this.paymentOriginId));
        return payment;
    }

    public List<PaymentDetail> toPaymentDetails() {
        List<PaymentDetail> paymentDetails = new ArrayList<>();
        if (FundUtils.isNull(PaymentDetails) || PaymentDetails.size() == 0)
            return null;
        for (PaymentDetailDto paymentDetail : PaymentDetails) {
            paymentDetails.add(paymentDetail.toPaymentDetail());
        }
        return paymentDetails;
    }

    private PaymentReason getPaymentReason(Long id) {
        return repository.findAll(PaymentReason.class).stream()
                .filter(a -> a.getId().equals(paymentReasonId))
                .findFirst()
                .orElse(null);
    }

    private SubsidiaryLedger getSubsidiaryLedger(Long id) {
        return repository.findAll(SubsidiaryLedger.class).stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private DetailLedger getDetailLedger(Long id) {
        return repository.findAll(DetailLedger.class).stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private Fund getFund(Long id) {
        return repository.findAll(Fund.class).stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private Bank getBank(Long id) {
        return repository.findAll(Bank.class).stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private PaymentType getPaymentType(Long id) {
        return repository.findAll(PaymentType.class).stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private PaymentStatus getPaymentStatus(Long id) {
        return repository.findAll(PaymentStatus.class).stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private PaymentOrigin getPaymentOrigin(Long id) {
        return repository.findAll(PaymentOrigin.class).stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
