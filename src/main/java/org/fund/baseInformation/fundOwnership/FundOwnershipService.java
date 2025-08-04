package org.fund.baseInformation.fundOwnership;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fund.accounting.detailLedger.DetailLedgerDto;
import org.fund.accounting.detailLedger.DetailLedgerService;
import org.fund.baseInformation.fundOwnership.dto.FundOwnershipRequest;
import org.fund.baseInformation.fundOwnership.dto.FundOwnershipResponse;
import org.fund.baseInformation.tradableItem.TradableItemGroup;
import org.fund.baseInformation.tradableItem.TradableItemService;
import org.fund.baseInformation.tradableItem.dto.TradableItemDto;
import org.fund.common.FundUtils;
import org.fund.constant.Consts;
import org.fund.exception.FundException;
import org.fund.exception.GeneralExceptionType;
import org.fund.model.DetailLedger;
import org.fund.model.FundOwnership;
import org.fund.model.TradableItemDetailLedger;
import org.fund.model.view.external.BourseFund;
import org.fund.model.view.external.Instrument;
import org.fund.model.view.internal.TradableItem;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FundOwnershipService {
    private final JpaRepository repository;

    public FundOwnershipService(JpaRepository jpaRepository) {
        this.repository = jpaRepository;
    }

    public void insert(FundOwnershipRequest fundOwnershipRequest, Long userId, String uuid) throws Exception {
        if (FundUtils.isNull(fundOwnershipRequest.getTradableItemId()))
            throw new FundException(GeneralExceptionType.FIELD_NOT_VALID, new Object[]{"tradableItemId"});
        if (FundUtils.isNull(fundOwnershipRequest.getTradableItemGroup()))
            throw new FundException(GeneralExceptionType.FIELD_NOT_VALID, new Object[]{"tradableItemGroup"});
        FundOwnership fundOwnership = FundOwnership.builder()
                .tradableItem(repository.findOne(TradableItem.class, fundOwnershipRequest.getTradableItemId()))
                .tradableItemGroup(fundOwnershipRequest.getTradableItemGroup().longValue())
                .build();
        repository.save(fundOwnership, userId, uuid);
    }

    public void delete(Long fundOwnershipId, Long userId, String uuid) throws Exception {
        repository.removeById(FundOwnership.class, fundOwnershipId, userId, uuid);
    }

    public List<FundOwnershipResponse> list(Long id) {
        List<FundOwnershipResponse> returnValue = new ArrayList<FundOwnershipResponse>();
        List<FundOwnership> fundOwnerships = new ArrayList<FundOwnership>();
        FundOwnershipResponse fundOwnershipResponse;
        if (!FundUtils.isNull(id))
            fundOwnerships = repository.findAll(FundOwnership.class).stream().filter(a -> a.getId().equals(id)).toList();
        else
            fundOwnerships = repository.findAll(FundOwnership.class);

        List<TradableItemDetailLedger> tradableItemDetailLedgers = repository.findAll(TradableItemDetailLedger.class);
        ObjectMapper objectMapper = new ObjectMapper();

        for (FundOwnership fundOwnership : fundOwnerships) {
            fundOwnershipResponse = FundOwnershipResponse.builder()
                    .id(fundOwnership.getId())
                    .detailLedger(getDetailLedgerDto(tradableItemDetailLedgers, fundOwnership.getId()))
                    .tradableItem(objectMapper.convertValue(fundOwnership.getTradableItem(), TradableItemDto.class))
                    .build();
            returnValue.add(fundOwnershipResponse);
        }
        return returnValue;
    }


    private DetailLedgerDto getDetailLedgerDto(List<TradableItemDetailLedger> tradableItemDetailLedgers, Long id) {
        if (FundUtils.isNull(id)) return null;
        TradableItemDetailLedger tradableItemDetailLedger = tradableItemDetailLedgers.stream()
                .filter(a -> a.getId().equals(id)).findFirst().orElse(null);
        if (FundUtils.isNull(tradableItemDetailLedger)) return null;
        return tradableItemDetailLedger.getDetailLedger().toDto();
    }
}
