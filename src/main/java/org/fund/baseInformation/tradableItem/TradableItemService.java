package org.fund.baseInformation.tradableItem;

import org.fund.accounting.detailLedger.DetailLedgerService;
import org.fund.accounting.detailLedger.constant.DetailLedgerType;
import org.fund.administration.fund.FundService;
import org.fund.common.FundUtils;
import org.fund.exception.FundException;
import org.fund.exception.GeneralExceptionType;
import org.fund.model.DetailLedger;
import org.fund.model.FinancialInstitutionBankAccount;
import org.fund.model.Fund;
import org.fund.model.TradableItemDetailLedger;
import org.fund.model.view.internal.TradableItem;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradableItemService {
    private final JpaRepository repository;
    private final DetailLedgerService detailLedgerService;

    public TradableItemService(JpaRepository repository, DetailLedgerService detailLedgerService) {
        this.repository = repository;
        this.detailLedgerService = detailLedgerService;
    }

    public DetailLedger insert(TradableItemDetailLedger tradableItemDetailLedger, Fund fund, Long userId, String uuid) throws Exception {
        TradableItem tradableItem = repository.findAll(TradableItem.class).stream()
                .filter(a -> a.getId().equals(tradableItemDetailLedger.getTradableItemId()) &&
                        FundUtils.longValue(a.getTradableItemGroup()).equals(tradableItemDetailLedger.getTradableItemGroupId()))
                .findFirst().orElse(null);
        if (FundUtils.isNull(tradableItem))
            throw new FundException(GeneralExceptionType.DATE_VALIDATION_FAILED, new Object[]{"tradableItemId", "tradableItemGroupId"});
        DetailLedgerType detailLedgerType = DetailLedgerType.getItemById(tradableItemDetailLedger.getTradableItemGroupId());
        DetailLedger detailLedger = detailLedgerService.get(tradableItem.getDescription(), fund, detailLedgerType);
        detailLedgerService.insert(detailLedger, userId, uuid);
        repository.save(detailLedger, userId, uuid);
        return detailLedger;
    }

    public void delete(Long tradableItemDetailLedgerId, Long userId, String uuid) throws Exception {
        TradableItemDetailLedger tradableItemDetailLedger = repository.findOne(TradableItemDetailLedger.class, tradableItemDetailLedgerId);
        detailLedgerService.delete(tradableItemDetailLedger.getDetailLedger().getId(), userId, uuid);
        repository.removeById(TradableItemDetailLedger.class, tradableItemDetailLedgerId, userId, uuid);
    }

    public List<TradableItemDetailLedger> list(Long tradableItemId) {
        if (!FundUtils.isNull(tradableItemId))
            return repository.findAll(TradableItemDetailLedger.class).stream()
                    .filter(a -> a.getId().equals(tradableItemId)).toList();
        return repository.findAll(TradableItemDetailLedger.class);
    }
}
