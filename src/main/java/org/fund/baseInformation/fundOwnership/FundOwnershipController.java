package org.fund.baseInformation.fundOwnership;

import org.fund.baseInformation.fundOwnership.dto.FundOwnershipRequest;
import org.fund.baseInformation.fundOwnership.dto.FundOwnershipResponse;
import org.fund.common.FundUtils;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.model.FundOwnership;
import org.fund.validator.NotEmpty;
import org.fund.validator.ValidateField;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class FundOwnershipController {
    private final FundOwnershipService service;

    public FundOwnershipController(FundOwnershipService service) {
        this.service = service;
    }

    @PostMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/fundOwnership/add")
    public void insert(@RequestBody FundOwnershipRequest fundOwnershipDto) throws Exception {
        service.insert(fundOwnershipDto, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @DeleteMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/fundOwnership/remove/{id}")
    public void remove(@PathVariable @NotEmpty(fieldName = "id")
                       @ValidateField(fieldName = "id", entityClass = FundOwnership.class) Long fundOwnershipId) throws Exception {
        service.delete(fundOwnershipId, RequestContext.getUserId(), RequestContext.getUuid());
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/fundOwnership/{id}")
    public FundOwnershipResponse list(@PathVariable("id") @ValidateField(fieldName = "id", entityClass = FundOwnership.class) Long fundOwnershipId) {
        List<FundOwnershipResponse> list = service.list(fundOwnershipId);
        return !FundUtils.isNull(list) ? list.get(0) : null;
    }

    @GetMapping(path = Consts.DEFAULT_VERSION_API_URL + "/baseInformation/fundOwnership")
    public List<FundOwnershipResponse> list() {
        return service.list(null);
    }
}
