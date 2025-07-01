package org.fund.baseInformation.tradableItem;

import org.fund.administration.fund.FundService;
import org.fund.baseInformation.financialInstitutionBankAccount.FinancialInstitutionBankAccountDto;
import org.fund.common.FundUtils;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.dto.GenericDtoMapper;
import org.fund.exception.FundException;
import org.fund.exception.GeneralExceptionType;
import org.fund.model.FinancialInstitutionBankAccount;
import org.fund.model.TradableItemDetailLedger;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping(Consts.DEFAULT_PREFIX_API_URL)
public class TradableItemController {
    private final TradableItemService service;
    private final FundService fundService;
    private final GenericDtoMapper mapper;

    public TradableItemController(TradableItemService service, FundService fundService, GenericDtoMapper mapper) {
        this.service = service;
        this.fundService = fundService;
        this.mapper = mapper;
    }

    @PostMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/tradableItem/detailLedger/add")
    public void insert(@RequestBody TradableItemDetailLedgerDto tradableItemDetailLedgerDto) throws Exception {
        service.insert(mapper.toEntity(TradableItemDetailLedger.class, tradableItemDetailLedgerDto), fundService.getDefaultFund(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/tradableItem/detailLedger/remove/{id}")
    public void remove(@PathVariable @NotEmpty(fieldName = "id")
                       @ValidateField(fieldName = "id", entityClass = TradableItemDetailLedger.class) Long tradableItemDetailLedgerId) throws Exception {
        service.delete(tradableItemDetailLedgerId, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/tradableItem/{id}")
    public TradableItemDetailLedgerDto getTradableItemDetailLedger(@PathVariable("id") @ValidateField(fieldName = "id", entityClass = TradableItemDetailLedger.class) Long tradableItemDetailLedgerId) {
        List<TradableItemDetailLedger> tradableItemDetailLedgers = service.list(tradableItemDetailLedgerId, null);
        return !FundUtils.isNull(tradableItemDetailLedgers) ? TradableItemDetailLedgerDto.toDto(tradableItemDetailLedgers.get(0)) : null;
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/tradableItem/tradableItemGroup/{id}")
    public List<TradableItemDetailLedgerDto> getAllTradableItemDetailLedgerByGroupId(@PathVariable("id") @NotEmpty(fieldName = "id") Long tradableItemGroupId) {
        if (FundUtils.isNull(TradableItemGroup.getItemById(tradableItemGroupId.intValue()))) {
            throw new FundException(GeneralExceptionType.FIELD_NOT_VALID, new Object[]{"id"});
        }
        List<TradableItemDetailLedger> tradableItemDetailLedgers = service.list(null, null).stream()
                .filter(a -> a.getTradableItemGroupId().equals(tradableItemGroupId)).toList();
        List<TradableItemDetailLedgerDto> tradableItemDetailLedgerDtos = new ArrayList<>();
        for (TradableItemDetailLedger ietm : tradableItemDetailLedgers) {
            tradableItemDetailLedgerDtos.add(TradableItemDetailLedgerDto.toDto(ietm));
        }
        return tradableItemDetailLedgerDtos;
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/tradableItem")
    public List<TradableItemDetailLedgerDto> getAllTradableItemDetailLedger() {
        List<TradableItemDetailLedger> tradableItemDetailLedgers = service.list(null, null);
        List<TradableItemDetailLedgerDto> tradableItemDetailLedgerDtos = new ArrayList<>();
        for (TradableItemDetailLedger ietm : tradableItemDetailLedgers) {
            tradableItemDetailLedgerDtos.add(TradableItemDetailLedgerDto.toDto(ietm));
        }
        return tradableItemDetailLedgerDtos;
    }
}
