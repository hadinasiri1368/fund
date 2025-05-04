package org.fund.baseInformation.tradableItem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.fund.model.DetailLedger;
import org.fund.model.DetailLedgerType;
import org.fund.model.FinancialInstitutionBankAccount;
import org.fund.model.TradableItemDetailLedger;
import org.fund.model.view.internal.TradableItem;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class TradableItemDetailLedgerDto {
    private Long id;
    @NotEmpty(fieldName = "tradableItemId")
    @ValidateField(fieldName = "tradableItemId", entityClass = TradableItem.class)
    private Long tradableItemId;
    @NotEmpty(fieldName = "tradableItemTypeId")
    private Long tradableItemTypeId;
    @NotEmpty(fieldName = "tradableItemGroupId")
    private Long tradableItemGroupId;
    private DetailLedger detailLedger;

    public TradableItemDetailLedger toTradableItemDetailLedger() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, TradableItemDetailLedger.class);
    }

    public static TradableItemDetailLedgerDto toDto(TradableItemDetailLedger tradableItemDetailLedger) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(tradableItemDetailLedger, TradableItemDetailLedgerDto.class);
    }

}
