package org.fund.accounting.detailLedger;

import org.fund.common.FundUtils;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.dto.GenericDtoMapper;
import org.fund.model.DetailLedger;
import org.fund.model.DetailLedgerType;
import org.fund.model.FundBranch;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping(Consts.DEFAULT_PREFIX_API_URL)
public class DetailLedgerController {
    private final DetailLedgerService service;
    private final GenericDtoMapper dtoMapper;

    public DetailLedgerController(DetailLedgerService service, GenericDtoMapper dtoMapper) {
        this.service = service;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping(path = Consts.DEFAULT_VERSION_API_URL + "/accounting/detailLedger/add")
    public void insert(@RequestBody DetailLedgerDto detailLedgerDto) throws Exception {
        service.insert(dtoMapper.toEntity(DetailLedger.class, detailLedgerDto), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PutMapping(path = Consts.DEFAULT_VERSION_API_URL + "/accounting/detailLedger/edit")
    public void edit(@RequestBody DetailLedgerDto detailLedgerDto) throws Exception {
        service.update(dtoMapper.toEntity(DetailLedger.class, detailLedgerDto), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(path = Consts.DEFAULT_VERSION_API_URL + "/accounting/detailLedger/remove/{id}")
    public void remove(@PathVariable @NotEmpty(fieldName = "id")
                       @ValidateField(fieldName = "id", entityClass = DetailLedger.class) Long detailLedgerId) throws Exception {
        service.delete(detailLedgerId, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/accounting/detailLedger/{id}")
    public DetailLedger getDetailLedgerList(@PathVariable("id") @ValidateField(fieldName = "id", entityClass = DetailLedger.class) Long detailLedgerId) {
        List<DetailLedger> detailLedgers = service.list(detailLedgerId);
        return !FundUtils.isNull(detailLedgers) ? detailLedgers.getFirst() : null;
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/accounting/detailLedger")
    public List<DetailLedger> getAllDetailLedgerList() {
        return service.list(null);
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/accounting/detailLedger/type")
    public List<DetailLedgerType> getDetailLedgerTypeList() {
        return service.getDetailLedgerType(null);
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/accounting/detailLedger/type/{id}")
    public DetailLedgerType getDetailLedgerType(@PathVariable("id") @ValidateField(fieldName = "id", entityClass = DetailLedgerType.class) Long detailLedgerTypeId) {
        List<DetailLedgerType> detailLedgerTypes = service.getDetailLedgerType(detailLedgerTypeId);
        return !FundUtils.isNull(detailLedgerTypes) ? detailLedgerTypes.getFirst() : null;
    }

}
