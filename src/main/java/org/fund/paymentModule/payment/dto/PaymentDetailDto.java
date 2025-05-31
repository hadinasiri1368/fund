package org.fund.paymentModule.payment.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.fund.model.*;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class PaymentDetailDto {
    private final JpaRepository repository;

    public PaymentDetailDto(JpaRepository repository) {
        this.repository = repository;
    }

    private Long id;
    @NotEmpty(fieldName = "paymentId")
    @ValidateField(fieldName = "paymentId" , entityClass = Payment.class)
    private Long paymentId;
    @NotEmpty(fieldName = "detailLedgerId")
    @ValidateField(fieldName = "detailLedgerId" , entityClass = Payment.class)
    private Long detailLedgerId;
    @ValidateField(fieldName = "paymentStatusId" , entityClass = PaymentStatus.class)
    private Long paymentStatusId;
    @ValidateField(fieldName = "bankAccountId" , entityClass = BankAccount.class)
    private Long bankAccountId;
//    @ValidateField(fieldName = "orderId" , entityClass = Order.class)
    private Long orderId;
    private String uuid;
    @NotEmpty(fieldName = "amount")
    private Long amount;
    private String comments;

    public PaymentDetail toPaymentDetail() {
        ObjectMapper objectMapper = new ObjectMapper();
        PaymentDetail paymentDetail = objectMapper.convertValue(this, PaymentDetail.class);
        paymentDetail.setPayment(getPayment(this.paymentId));
        paymentDetail.setDetailLedger(getDetailLedger(this.detailLedgerId));
        paymentDetail.setPaymentStatus(getPaymentStatus(this.paymentStatusId));
        return paymentDetail;
    }

    private Payment getPayment(Long id) {
        return repository.findAll(Payment.class).stream()
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

    private PaymentStatus getPaymentStatus(Long id) {
        return repository.findAll(PaymentStatus.class).stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
