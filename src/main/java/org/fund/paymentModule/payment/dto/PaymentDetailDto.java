package org.fund.paymentModule.payment.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.fund.dto.DtoConvertible;
import org.fund.model.*;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
public class PaymentDetailDto implements DtoConvertible {
    private Long id;
    @NotEmpty(fieldName = "paymentId")
    @ValidateField(fieldName = "paymentId", entityClass = Payment.class)
    private Long paymentId;
    @NotEmpty(fieldName = "detailLedgerId")
    @ValidateField(fieldName = "detailLedgerId", entityClass = Payment.class)
    private Long detailLedgerId;
    @ValidateField(fieldName = "paymentStatusId", entityClass = PaymentStatus.class)
    private Long paymentStatusId;
    @ValidateField(fieldName = "bankAccountId", entityClass = BankAccount.class)
    private Long bankAccountId;
    //    @ValidateField(fieldName = "orderId" , entityClass = Order.class)
    private Long orderId;
    private String uuid;
    @NotEmpty(fieldName = "amount")
    private Long amount;
    private String comments;

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        ObjectMapper objectMapper = new ObjectMapper();
        T entity = objectMapper.convertValue(this, targetType);

        if (entity instanceof PaymentDetail paymentDetail) {
            paymentDetail.setPayment(getPayment(repository, paymentId));
            paymentDetail.setDetailLedger(getDetailLedger(repository, detailLedgerId));
            paymentDetail.setPaymentStatus(getPaymentStatus(repository, paymentStatusId));
        }

        return entity;
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        return List.of();
    }
    
    private Payment getPayment(JpaRepository repository, Long id) {
        return repository.findAll(Payment.class).stream()
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

    private PaymentStatus getPaymentStatus(JpaRepository repository, Long id) {
        return repository.findAll(PaymentStatus.class).stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
