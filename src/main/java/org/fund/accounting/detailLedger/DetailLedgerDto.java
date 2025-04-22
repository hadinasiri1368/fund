package org.fund.accounting.detailLedger;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.fund.model.AccountNature;
import org.fund.model.Customer;
import org.fund.model.DetailLedger;
import org.fund.model.DetailLedgerType;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
public class DetailLedgerDto {
    private final JpaRepository repository;

    public DetailLedgerDto(JpaRepository repository) {
        this.repository = repository;
    }

    private Long id;
    private String name;
    private String code;
    private Long detailLedgerTypeId;
    private Boolean isActive;

    public DetailLedger toDetailLedger() {
        ObjectMapper objectMapper = new ObjectMapper();
        DetailLedger detailLedger = objectMapper.convertValue(this, DetailLedger.class);
        detailLedger.setDetailLedgerType(getDetailLedgerType(detailLedgerTypeId));
        return detailLedger;
    }

    private DetailLedgerType getDetailLedgerType(Long detailLedgerTypeId) {
        return repository.findAll(DetailLedgerType.class).stream()
                .filter(a -> a.getId().equals(detailLedgerTypeId))
                .findFirst()
                .orElse(null);
    }
}
