package org.fund.paymentModule.paymentReason;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Builder;
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
public class PaymentReasonDto {
    private final JpaRepository repository;

    public PaymentReasonDto(JpaRepository repository) {
        this.repository = repository;
    }

    private Long id;
    @NotEmpty(fieldName = "name")
    private String name;
    private Boolean systemDefined;
    @ValidateField(fieldName = "fromSubsidiaryLedgerId", entityClass = SubsidiaryLedger.class)
    private Long fromSubsidiaryLedgerId;
    @ValidateField(fieldName = "toSubsidiaryLedgerId", entityClass = SubsidiaryLedger.class)
    private Long toSubsidiaryLedgerId;
    @ValidateField(fieldName = "fromDetailLedgerId", entityClass = DetailLedger.class)
    private Long fromDetailLedgerId;
    @ValidateField(fieldName = "toDetailLedgerId", entityClass = DetailLedger.class)
    private Long toDetailLedgerId;

    public PaymentReason toPaymentReason() {
        ObjectMapper objectMapper = new ObjectMapper();
        PaymentReason paymentReason = objectMapper.convertValue(this, PaymentReason.class);
        paymentReason.setFromSubsidiaryLedger(getSubsidiaryLedger(this.fromSubsidiaryLedgerId));
        paymentReason.setToSubsidiaryLedger(getSubsidiaryLedger(this.toSubsidiaryLedgerId));
        paymentReason.setFromDetailLedger(getDetailLedger(this.fromDetailLedgerId));
        paymentReason.setToDetailLedger(getDetailLedger(this.toDetailLedgerId));
        return paymentReason;
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
}
