package org.fund.paymentModule.paymentReason;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Builder;
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
public class PaymentReasonDto implements DtoConvertible {
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

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        ObjectMapper objectMapper = new ObjectMapper();
        T entity = objectMapper.convertValue(this, targetType);

        if (entity instanceof PaymentReason paymentReason) {
            paymentReason.setFromSubsidiaryLedger(getSubsidiaryLedger(repository, fromSubsidiaryLedgerId));
            paymentReason.setToSubsidiaryLedger(getSubsidiaryLedger(repository, toSubsidiaryLedgerId));
            paymentReason.setFromDetailLedger(getDetailLedger(repository, fromDetailLedgerId));
            paymentReason.setToDetailLedger(getDetailLedger(repository, toDetailLedgerId));
        }

        return entity;
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        return List.of();
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
}
