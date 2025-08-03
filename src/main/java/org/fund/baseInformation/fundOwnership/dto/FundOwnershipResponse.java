package org.fund.baseInformation.fundOwnership.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.fund.accounting.detailLedger.DetailLedgerDto;
import org.fund.baseInformation.tradableItem.dto.TradableItemDto;
import org.fund.dto.DtoConvertible;
import org.fund.model.DetailLedger;
import org.fund.model.DetailLedgerType;
import org.fund.model.FundOwnership;
import org.fund.model.view.external.BourseFund;
import org.fund.model.view.external.Instrument;
import org.fund.model.view.internal.TradableItem;
import org.fund.repository.JpaRepository;

import java.util.List;

@Getter
@Setter
@Builder
public class FundOwnershipResponse implements DtoConvertible {
    private Long id;
    private TradableItemDto tradableItem;
    private DetailLedgerDto detailLedger;

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
         return null;
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        return List.of();
    }
}
