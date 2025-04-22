package org.fund.accounting.detailLedger;

import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.model.DetailLedger;
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

    public DetailLedgerController(DetailLedgerService service) {
        this.service = service;
    }

    @PostMapping(path = Consts.DEFAULT_VERSION_API_URL + "/accounting/detailLedger/add")
    public void insert(@RequestBody DetailLedgerDto detailLedgerDto) throws Exception {
        service.insert(detailLedgerDto.toDetailLedger(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @PutMapping(path = Consts.DEFAULT_VERSION_API_URL + "/accounting/detailLedger/edit")
    public void edit(@RequestBody DetailLedgerDto detailLedgerDto) throws Exception {
        service.update(detailLedgerDto.toDetailLedger(), RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(path = Consts.DEFAULT_VERSION_API_URL + "/accounting/detailLedger/remove/{id}")
    public void remove(@PathVariable @NotEmpty(fieldName = "id")
                       @ValidateField(fieldName = "id", entityClass = DetailLedger.class) Long detailLedgerId) throws Exception {
        service.delete(detailLedgerId, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/accounting/detailLedger/{id}")
    public List<DetailLedger> getDetailLedgerList(@PathVariable @ValidateField(fieldName = "id", entityClass = DetailLedger.class) Long detailLedgerId) {
        return service.list(detailLedgerId);
    }
}
