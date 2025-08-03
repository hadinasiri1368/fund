package org.fund.baseInformation.tradableItem.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.fund.dto.DtoConvertible;
import org.fund.model.TradableItemDetailLedger;
import org.fund.model.view.internal.TradableItem;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;

import java.util.List;

@Getter
@Setter
public class TradableItemDetailLedgerDto implements DtoConvertible {
    private Long id;
    @NotEmpty(fieldName = "tradableItemId")
    @ValidateField(fieldName = "id", entityClass = TradableItem.class)
    private Long tradableItemId;
    @NotEmpty(fieldName = "tradableItemTypeId")
    private Long tradableItemTypeId;
    @NotEmpty(fieldName = "tradableItemGroupId")
    private Long tradableItemGroupId;

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(this, targetType);
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        return List.of();
    }

    public static TradableItemDetailLedgerDto toDto(TradableItemDetailLedger tradableItemDetailLedger) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(tradableItemDetailLedger, TradableItemDetailLedgerDto.class);
    }

}
