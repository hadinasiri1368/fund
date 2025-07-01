package org.fund.accounting.detailLedger;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.fund.dto.DtoConvertible;
import org.fund.model.AccountNature;
import org.fund.model.Customer;
import org.fund.model.DetailLedger;
import org.fund.model.DetailLedgerType;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;


@Getter
@Setter
public class DetailLedgerDto implements DtoConvertible {
    private Long id;
    private String name;
    private String code;
    private Long detailLedgerTypeId;
    private Boolean isActive;

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        ObjectMapper objectMapper = new ObjectMapper();
        T entity = objectMapper.convertValue(this, targetType);

        if (entity instanceof DetailLedger detailLedger) {
            detailLedger.setDetailLedgerType(getDetailLedgerType(repository, detailLedgerTypeId));
        }

        return entity;
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        return List.of();
    }

    private DetailLedgerType getDetailLedgerType(JpaRepository repository, Long detailLedgerTypeId) {
        return repository.findOne(DetailLedgerType.class, detailLedgerTypeId);
    }
}
