package org.fund.baseInformation.fundOwnership.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.fund.dto.DtoConvertible;
import org.fund.model.DetailLedger;
import org.fund.model.Fund;
import org.fund.model.FundBranch;
import org.fund.model.FundOwnership;
import org.fund.model.view.external.BourseFund;
import org.fund.model.view.external.Instrument;
import org.fund.model.view.internal.TradableItem;
import org.fund.repository.JpaRepository;
import org.fund.validator.NotEmpty;

import java.util.List;

@Getter
@Setter
@Builder
public class FundOwnershipRequest implements DtoConvertible {
    private Long id;
    private Long tradableItemId;
    private Integer tradableItemGroup;
    private Long fundId;

    @Override
    public <T> T toEntity(Class<T> targetType, JpaRepository repository) {
        ObjectMapper objectMapper = new ObjectMapper();
        T entity = objectMapper.convertValue(this, targetType);

        if (entity instanceof FundOwnership fundOwnership) {
            fundOwnership.setTradableItem(getTradableItem(repository, tradableItemId, tradableItemGroup));
            fundOwnership.setTradableItemGroup(tradableItemGroup.longValue());
            fundOwnership.setFund(getFund(repository, fundId));
        }

        return entity;
    }

    @Override
    public <T> List<T> toEntityList(Class<T> entityClass, JpaRepository repository) {
        return List.of();
    }

    private TradableItem getTradableItem(JpaRepository repository, Long tradableItemId, Integer tradableItemGroup) {
        return repository.findAll(TradableItem.class).stream()
                .filter(a -> a.getId().equals(tradableItemId) && a.getTradableItemGroup().equals(tradableItemGroup))
                .findFirst().orElse(null);
    }

    private Fund getFund(JpaRepository repository, Long fundId) {
        return repository.findOne(Fund.class, fundId);
    }
}
