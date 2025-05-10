package org.fund.baseInformation.fundOwnership;

import org.fund.baseInformation.fundOwnership.dto.FundOwnershipRequest;
import org.fund.baseInformation.fundOwnership.dto.FundOwnershipResponse;
import org.fund.baseInformation.tradableItem.TradableItemGroup;
import org.fund.baseInformation.tradableItem.TradableItemService;
import org.fund.common.FundUtils;
import org.fund.constant.Consts;
import org.fund.exception.FundException;
import org.fund.exception.GeneralExceptionType;
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
    private final TradableItemService tradableItemService;

    public FundOwnershipService(JpaRepository jpaRepository, TradableItemService tradableItemService) {
        this.repository = jpaRepository;
        this.tradableItemService = tradableItemService;
    }

    public void insert(FundOwnershipRequest fundOwnershipRequest, Long userId, String uuid) throws Exception {
        if ((!FundUtils.isNull(fundOwnershipRequest.getBourseFundId()) && !FundUtils.isNull(fundOwnershipRequest.getInstrumentId())) || (FundUtils.isNull(fundOwnershipRequest.getBourseFundId()) && FundUtils.isNull(fundOwnershipRequest.getInstrumentId())))
            throw new FundException(GeneralExceptionType.FIELD_NOT_VALID, new Object[]{"bourseFundId,instrumentId"});
        FundOwnership fundOwnership = FundOwnership.builder().bourseFund(repository.findOne(BourseFund.class, fundOwnershipRequest.getBourseFundId())).instrument(repository.findOne(Instrument.class, fundOwnershipRequest.getInstrumentId())).build();
        repository.save(fundOwnership, userId, uuid);
    }

    public void delete(Long fundOwnershipId, Long userId, String uuid) throws Exception {
        repository.removeById(FundOwnership.class, fundOwnershipId, userId, uuid);
    }

    public List<FundOwnershipResponse> list(Long id) {
        List<FundOwnershipResponse> returnValue = new ArrayList<FundOwnershipResponse>();
        List<TradableItemDetailLedger> list = tradableItemService.list(null, TradableItemGroup.FUND);
        List<BourseFund> bourseFunds = repository.findAll(BourseFund.class);
        if (!FundUtils.isNull(list) && list.size() > 0) {
            for (TradableItemDetailLedger tradableItemService : list) {
                BourseFund bourseFund = bourseFunds.stream().filter(a -> a.getId().equals(tradableItemService.getTradableItemId()) && a.getIsActive()).findFirst().orElse(null);
                returnValue.add(FundOwnershipResponse.builder().bourseFund(bourseFund).detailLedger(tradableItemService.getDetailLedger()).id(!FundUtils.isNull(bourseFund) ? bourseFund.getId() : null).build());
            }
        }
        Long[] ids = new Long[]{Consts.INSTRUMENT_TYPE_ETF1, Consts.INSTRUMENT_TYPE_ETF2, Consts.INSTRUMENT_TYPE_ETF3, Consts.INSTRUMENT_TYPE_ETF4, Consts.INSTRUMENT_TYPE_ETF_IME, Consts.INSTRUMENT_TYPE_ETF_VENTURE};
        String hql = "select i from instrument i where i.instrumentType.id in (:instrumentTypeId)";
        Map<String, Object> params = new HashMap<>();
        params.put("instrumentTypeId", ids);
        List<Instrument> instruments = repository.listObjectByQuery(hql, params);
        list = tradableItemService.list(null, TradableItemGroup.INSTRUMENT);
        if (!FundUtils.isNull(instruments) && instruments.size() > 0) {
            for (Instrument instrument : instruments) {
                TradableItemDetailLedger tradableItemDetailLedger = list.stream().filter(a -> a.getId().equals(instrument.getId())).findFirst().orElse(null);
                returnValue.add(FundOwnershipResponse.builder().instrument(instrument).detailLedger(!FundUtils.isNull(tradableItemDetailLedger) ? tradableItemDetailLedger.getDetailLedger() : null).id(!FundUtils.isNull(tradableItemDetailLedger) ? tradableItemDetailLedger.getId() : null).build());
            }
        }
        if (FundUtils.isNull(id)) return returnValue;
        return returnValue.stream().filter(a -> a.getId().equals(id)).toList();
    }
}
