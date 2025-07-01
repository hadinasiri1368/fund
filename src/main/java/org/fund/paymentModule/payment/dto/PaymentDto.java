package org.fund.paymentModule.payment.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.fund.accounting.voucher.dto.VoucherDetailDto;
import org.fund.common.FundUtils;
import org.fund.dto.DtoConvertible;
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
public class PaymentDto implements DtoConvertible {
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

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        ObjectMapper objectMapper = new ObjectMapper();
        T entity = objectMapper.convertValue(this, targetType);

        if (entity instanceof Payment payment) {
            payment.setPaymentReason(getPaymentReason(repository, paymentReasonId));
            payment.setFromSubsidiaryLedger(getSubsidiaryLedger(repository, fromSubsidiaryLedgerId));
            payment.setToSubsidiaryLedger(getSubsidiaryLedger(repository, toSubsidiaryLedgerId));
            payment.setFromDetailLedger(getDetailLedger(repository, fromDetailLedgerId));
            payment.setFund(getFund(repository, fundId));
            payment.setBank(getBank(repository, bankId));
            payment.setPaymentType(getPaymentType(repository, paymentTypeId));
            payment.setPaymentStatus(getPaymentStatus(repository, paymentStatusId));
            payment.setPaymentOrigin(getPaymentOrigin(repository, paymentOriginId));
        }

        return entity;
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        List<T> entities = new ArrayList<>();
        for (PaymentDetailDto paymentDetailDto : PaymentDetails) {
            entities.add(paymentDetailDto.toEntity(entityClass, repository));
        }
        return entities;
    }

    private PaymentReason getPaymentReason(JpaRepository repository, Long id) {
        return repository.findAll(PaymentReason.class).stream()
                .filter(a -> a.getId().equals(paymentReasonId))
                .findFirst()
                .orElse(null);
    }

    private SubsidiaryLedger getSubsidiaryLedger(JpaRepository repository, Long id) {
        return repository.findAll(SubsidiaryLedger.class).stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private DetailLedger getDetailLedger(JpaRepository repository, Long id) {
        return repository.findAll(DetailLedger.class).stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private Fund getFund(JpaRepository repository, Long id) {
        return repository.findAll(Fund.class).stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private Bank getBank(JpaRepository repository, Long id) {
        return repository.findAll(Bank.class).stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private PaymentType getPaymentType(JpaRepository repository, Long id) {
        return repository.findAll(PaymentType.class).stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private PaymentStatus getPaymentStatus(JpaRepository repository, Long id) {
        return repository.findAll(PaymentStatus.class).stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private PaymentOrigin getPaymentOrigin(JpaRepository repository, Long id) {
        return repository.findAll(PaymentOrigin.class).stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
